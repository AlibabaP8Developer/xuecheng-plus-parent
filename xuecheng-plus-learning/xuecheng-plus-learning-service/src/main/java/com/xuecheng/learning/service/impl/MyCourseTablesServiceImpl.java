package com.xuecheng.learning.service.impl;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.learning.mapper.XcChooseCourseMapper;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.service.MyCourseTablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyCourseTablesServiceImpl implements MyCourseTablesService {
    @Autowired
    XcChooseCourseMapper chooseCourseMapper;

    @Override
    public boolean saveChooseCourseSuccess(String chooseCourseId) {
        // 根据选课ID查询选课表
        XcChooseCourse xcChooseCourse = chooseCourseMapper.selectById(chooseCourseId);
        if (xcChooseCourse == null) {
            log.debug("接收购买课程的消息，根据选课ID从数据库找不到选课记录，选课ID：{}", chooseCourseId);
            return false;
        }

        // 选课状态
        String status = xcChooseCourse.getStatus();
        // 只有当未支付时才更新为已支付
        if ("701002".equals(status)) {
            // 更新选课记录的状态为支付成功
            xcChooseCourse.setStatus("701001");
            int i = chooseCourseMapper.updateById(xcChooseCourse);
            if (i <= 0) {
                log.debug("添加选课记录失败:{}", xcChooseCourse);
                XueChengPlusException.cast("添加选课记录失败");
            }
            // 向课程表插入记录

        }
        return false;
    }
}
