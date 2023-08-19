package com.xuecheng.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.utils.IdWorkerUtils;
import com.xuecheng.base.utils.QRCodeUtil;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xuecheng.orders.config.AlipayConfig;
import com.xuecheng.orders.config.PayNotifyConfig;
import com.xuecheng.orders.mapper.XcOrdersGoodsMapper;
import com.xuecheng.orders.mapper.XcOrdersMapper;
import com.xuecheng.orders.mapper.XcPayRecordMapper;
import com.xuecheng.orders.model.dto.AddOrderDto;
import com.xuecheng.orders.model.dto.PayRecordDto;
import com.xuecheng.orders.model.dto.PayStatusDto;
import com.xuecheng.orders.model.po.XcOrders;
import com.xuecheng.orders.model.po.XcOrdersGoods;
import com.xuecheng.orders.model.po.XcPayRecord;
import com.xuecheng.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Value("${pay.alipay.APP_ID}")
    String APP_ID;

    @Value("${pay.alipay.APP_PRIVATE_KEY}")
    String APP_PRIVATE_KEY;

    @Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
    String ALIPAY_PUBLIC_KEY;

    @Autowired
    XcOrdersMapper ordersMapper;

    @Autowired
    XcOrdersGoodsMapper ordersGoodsMapper;

    @Autowired
    XcPayRecordMapper payRecordMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MqMessageService mqMessageService;

    @Value("${pay.qrcodeurl}")
    String qrcodeurl;

    @Transactional
    @Override
    public PayRecordDto createOrder(String userId, AddOrderDto addOrderDto) {
        // 插入订单表，订单主表和订单明细表
        XcOrders xcOrders = saveXcOrders(userId, addOrderDto);

        //插入支付记录
        XcPayRecord payRecord = createPayRecord(xcOrders);
        Long payNo = payRecord.getPayNo();

        //生成二维码
        QRCodeUtil qrCodeUtil = new QRCodeUtil();
        // 支付二维码URL
        String url = String.format(qrcodeurl, payNo);
        // 二维码图片
        String qrCode = "";
        try {
            qrCode = qrCodeUtil.createQRCode(url, 200, 200);
        } catch (IOException e) {
            XueChengPlusException.cast("生成二维码出错");
        }

        PayRecordDto payRecordDto = new PayRecordDto();
        BeanUtils.copyProperties(payRecord, payRecordDto);
        payRecordDto.setQrcode(qrCode);
        return payRecordDto;
    }

    @Override
    public XcPayRecord getPayRecordByPayno(String payNo) {
        XcPayRecord xcPayRecord = payRecordMapper.selectOne(new LambdaQueryWrapper<XcPayRecord>().eq(XcPayRecord::getPayNo, payNo));
        return xcPayRecord;
    }

    @Override
    public PayRecordDto queryPayResult(String payNo) {
        // 调用支付宝的接口查询支付结果
        PayStatusDto payStatusDto = queryPayResultFromAlipay(payNo);
        // 拿到支付结果更新支付记录表和订单表的支付状态
        saveAliPayStatus(payStatusDto);
        return null;
    }

    /**
     * 请求支付宝查询支付结果
     *
     * @param payNo 支付交易号
     * @return 支付结果
     */
    public PayStatusDto queryPayResultFromAlipay(String payNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, APP_ID, APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE); //获得初始化的AlipayClient
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", payNo);
        request.setBizContent(bizContent.toString());
        String body = "";
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (!response.isSuccess()){
                // 交易不成功
                XueChengPlusException.cast("请求支付宝查询支付结果失败");
            }
            body = response.getBody();
        } catch (AlipayApiException e) {
            log.error("请求支付宝查询支付结果异常:{}", e.toString(), e);
            XueChengPlusException.cast("请求支付查询查询失败");
        }

        Map<String, Object> resultMap = JSON.parseObject(body, Map.class);
        Map alipayTradeQueryResponse = (Map) resultMap.get("alipay_trade_query_response");

        String tradeNo = (String) alipayTradeQueryResponse.get("trade_no");
        //支付结果
        String tradeStatus = (String) alipayTradeQueryResponse.get("trade_status");
        String totalAmount = (String) alipayTradeQueryResponse.get("total_amount");

        // 解析支付结果
        PayStatusDto payStatusDto = new PayStatusDto();
        payStatusDto.setOut_trade_no(payNo);
        payStatusDto.setTrade_no(tradeNo);
        payStatusDto.setTrade_status(tradeStatus);
        payStatusDto.setApp_id(APP_ID);
        payStatusDto.setTotal_amount(totalAmount);
        return payStatusDto;
    }

    /**
     * @param payStatusDto 支付结果信息
     * @return void
     * @description 保存支付宝支付结果
     * @author Mr.M
     * @date 2022/10/4 16:52
     */
    public void saveAliPayStatus(PayStatusDto payStatusDto) {

    }

    /**
     * 保存支付记录
     *
     * @param orders
     * @return
     */
    public XcPayRecord createPayRecord(XcOrders orders) {
        // 订单ID
        Long orderId = orders.getId();
        XcOrders xcOrders = ordersMapper.selectById(orderId);

        // 如果此订单不存在不能添加支付记录
        if (xcOrders == null) {
            XueChengPlusException.cast("订单不存在");
        }

        // 订单状态
        String status = xcOrders.getStatus();
        if ("601002".equals(status)) { // 支付成功
            XueChengPlusException.cast("此订单已支付");
        }
        // 添加支付记录
        XcPayRecord xcPayRecord = new XcPayRecord();
        // 支付记录号，将来要传给支付宝
        xcPayRecord.setPayNo(IdWorkerUtils.getInstance().nextId());
        xcPayRecord.setOrderId(orderId);
        xcPayRecord.setOrderName(xcOrders.getOrderName());
        xcPayRecord.setTotalPrice(xcOrders.getTotalPrice());
        xcPayRecord.setCurrency("CNY");
        xcPayRecord.setCreateDate(LocalDateTime.now());
        // 未支付
        xcPayRecord.setStatus("601001");
        xcPayRecord.setUserId(xcOrders.getUserId());

        int insert = payRecordMapper.insert(xcPayRecord);
        if (insert <= 0) XueChengPlusException.cast("插入支付记录失败");

        // 如果此订单支付结果为成功，不再添加支付记录，避免重复支付

        return xcPayRecord;
    }

    /**
     * 保存订单信息
     *
     * @param userId
     * @param addOrderDto
     * @return
     */
    @Transactional
    public XcOrders saveXcOrders(String userId, AddOrderDto addOrderDto) {

        //进行幂等性判断，同一个选课记录只能有一个订单
        XcOrders xcOrders = getOrderByBusinessId(addOrderDto.getOutBusinessId());
        if (xcOrders != null) {
            return xcOrders;
        }
        //插入订单主标
        xcOrders = new XcOrders();
        // 使用雪花算法生成订单号
        xcOrders.setId(IdWorkerUtils.getInstance().nextId());
        xcOrders.setTotalPrice(addOrderDto.getTotalPrice());
        xcOrders.setCreateDate(LocalDateTime.now());
        // 未支付
        xcOrders.setStatus("600001");
        xcOrders.setUserId(userId);
        // 订单类型
        xcOrders.setOrderType("60201");
        xcOrders.setOrderName(addOrderDto.getOrderName());
        xcOrders.setOrderDescrip(addOrderDto.getOrderDescrip());
        xcOrders.setOrderDetail(addOrderDto.getOrderDetail());
        // 如果是选课这里记录了选课表ID
        xcOrders.setOutBusinessId(addOrderDto.getOutBusinessId());
        int insert = ordersMapper.insert(xcOrders);
        if (insert <= 0) {
            XueChengPlusException.cast("插入订单失败");
        }
        // 订单ID
        Long id = xcOrders.getId();
        // 将前端传入的明细json转成list
        String orderDetailJson = addOrderDto.getOrderDetail();
        List<XcOrdersGoods> xcOrdersGoods = JSON.parseArray(orderDetailJson, XcOrdersGoods.class);
        // 遍历xcOrdersGoods插入订单明细表
        xcOrdersGoods.forEach(goods -> {
            goods.setOrderId(id);
            //插入订单明细表
            ordersGoodsMapper.insert(goods);
        });

        return xcOrders;
    }

    /**
     * 根据业务id查询订单
     *
     * @param businessId: 业务ID就是选课记录表中的ID主键
     * @return
     */
    public XcOrders getOrderByBusinessId(String businessId) {
        XcOrders orders = ordersMapper.selectOne(new LambdaQueryWrapper<XcOrders>().eq(XcOrders::getOutBusinessId, businessId));
        return orders;
    }

    @Override
    public void notifyPayResult(MqMessage message) {
        //消息内容
        String jsonString = JSON.toJSONString(message);

        // 持久化消息
        Message messageObj = MessageBuilder.withBody(jsonString.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();

        Long id = message.getId();

        // 全局的消息ID
        CorrelationData correlationData = new CorrelationData(id.toString());
        // 使用correlationData指定回调方法
        correlationData.getFuture().addCallback(result -> {
            if (result.isAck()) {
                // 消息成功发送到了交换机
                log.debug("发送消息成功:{}", jsonString);
                // 完成消息，将消息从数据库表删除
                mqMessageService.completed(id);
            } else {
                // 消息发送失败
                log.debug("发送消息失败:{}", jsonString);
            }
        }, ex -> {
            // 发生异常了
            log.debug("发送消息异常:{}", jsonString);
        });
        // 发送消息
        rabbitTemplate.convertAndSend(PayNotifyConfig.PAYNOTIFY_EXCHANGE_FANOUT, "", messageObj, correlationData);
    }
}
