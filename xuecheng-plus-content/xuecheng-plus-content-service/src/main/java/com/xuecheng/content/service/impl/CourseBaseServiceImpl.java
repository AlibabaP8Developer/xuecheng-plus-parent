package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CommonError;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CourseMarketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseInfoService {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Autowired
    CourseMarketService courseMarketService;

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {
        // 对参数进行合法性的校验
        //合法性校验
        //if (StringUtils.isBlank(dto.getName())) {
        //XueChengPlusException.cast("课程名称为空");
        //XueChengPlusException.cast(CommonError.PARAMS_ERROR);
        //}

        //if (StringUtils.isBlank(dto.getMt())) {
        //    throw new RuntimeException("课程分类为空");
        //}
        //
        //if (StringUtils.isBlank(dto.getSt())) {
        //    throw new RuntimeException("课程分类为空");
        //}

        //if (StringUtils.isBlank(dto.getGrade())) {
        //    throw new RuntimeException("课程等级为空");
        //}
        //
        //if (StringUtils.isBlank(dto.getTeachmode())) {
        //    throw new RuntimeException("教育模式为空");
        //}
        //
        //if (StringUtils.isBlank(dto.getUsers())) {
        //    throw new RuntimeException("适应人群为空");
        //}

        //if (StringUtils.isBlank(dto.getCharge())) {
        //    throw new RuntimeException("收费规则为空");
        //}

        // 对数据进行封装，调用mapper进行数据持久化
        CourseBase courseBase = new CourseBase();
        // 将传入dto的数据设置到courseBase对象, dto拷贝到courseBase里
        BeanUtils.copyProperties(dto, courseBase);
        // 设置机构ID
        courseBase.setCompanyId(companyId);
        // 创建时间
        courseBase.setCreateDate(LocalDateTime.now());
        // 审核状态默认为未提交
        courseBase.setAuditStatus("202002");
        // 发布状态默认为未发布
        courseBase.setStatus("203001");
        // 课程基本表插入一条记录
        int insert = courseBaseMapper.insert(courseBase);
        // 获取课程ID
        Long courseId = courseBase.getId();
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);
        courseMarket.setId(courseId);
        // 校验如果课程为收费，价格必须输入 且必须大于0
        String charge = dto.getCharge();
        if (charge.equals("201001")) {
            float price = courseMarket.getPrice();
            if (courseMarket.getPrice() == null || price <= 0) {
                XueChengPlusException.cast("课程为收费，但是价格为空且必须大于0");
            }
        }
        // 向课程营销表插入一条记录
        int insert1 = courseMarketMapper.insert(courseMarket);
        if (insert < 0 || insert1 <= 0) {
            //throw new RuntimeException("添加课程失败");
            XueChengPlusException.cast("添加课程失败");
        }
        // 组装要返回的结果
        return getCourseBaseInfo(courseId);
    }

    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {
        // 校验
        // 课程id
        Long courseId = dto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            XueChengPlusException.cast("课程不存在");
        }

        // 校验本机构只能修改本机构的课程
        //if (!companyId.equals(courseBase.getCompanyId())) {
            //XueChengPlusException.cast("只允许修改本机构的课程");
        //}

        // 封装基本信息的数据
        BeanUtils.copyProperties(dto, courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        // 更新课程基本信息
        courseBaseMapper.updateById(courseBase);

        // 封装营销信息的数据
        // 查询营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);

        // 校验如果课程设置了收费，价格不能为空且必须大于0
        //String charge = courseMarket.getCharge();
        //if (charge.equals("201001")) {
        //    Float price = courseMarket.getPrice();
        //    if (price == null || price.floatValue() <= 0) {
        //        XueChengPlusException.cast("课程设置了收费价格不能为空且必须大于0");
        //    }
        //}

        saveCourseMarket(courseMarket);

        // 请求数据库
        // 对营销表有则更新，没有则增加
        courseMarketService.saveOrUpdate(courseMarket);

        // 查询课程信息
        return this.getCourseBaseInfo(courseId);
    }

    // 抽取对营销的保存
    private int saveCourseMarket(CourseMarket courseMarket) {

        // 校验如果课程设置了收费，价格不能为空且必须大于0
        String charge = courseMarket.getCharge();
        if (StringUtils.isBlank(charge)) {
            XueChengPlusException.cast("收费规则没有选择");
        }
        if (charge.equals("201001")) {
            Float price = courseMarket.getPrice();
            if (price == null || price.floatValue() <= 0) {
                XueChengPlusException.cast("课程设置了收费价格不能为空且必须大于0");
            }
        }

        boolean b = courseMarketService.saveOrUpdate(courseMarket);

        return b ? 1 : 0;
    }

    /**
     * 根据课程ID查询课程的基本信息、营销信息
     *
     * @param courseId 课程ID
     * @return 课程信息
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        // 基本信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        // 营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);

        // 根据课程分类的ID查询分类的名称
        String mt = courseBase.getMt();
        String st = courseBase.getSt();
        CourseCategory mtCategory = courseCategoryMapper.selectById(mt);
        CourseCategory stCategory = courseCategoryMapper.selectById(st);
        if (mtCategory != null) {
            // 分类名称
            String mtName = mtCategory.getName();
            courseBaseInfoDto.setMtName(mtName);
        }
        if (stCategory != null) {
            // 分类名称
            String stName = stCategory.getName();
            courseBaseInfoDto.setStName(stName);
        }
        return courseBaseInfoDto;
    }


    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto paramsDto) {
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        // 拼接查询条件
        // 根据课程名称模糊查询
        wrapper.like(StringUtils.isNotBlank(paramsDto.getCourseName()), CourseBase::getName, paramsDto.getCourseName());
        // 根据课程审核状态
        wrapper.eq(StringUtils.isNotEmpty(paramsDto.getAuditStatus()), CourseBase::getAuditStatus, paramsDto.getAuditStatus());
        // 根据课程发布状态
        wrapper.eq(StringUtils.isNotEmpty(paramsDto.getPublishStatus()), CourseBase::getStatus, paramsDto.getPublishStatus());
        wrapper.orderByDesc(CourseBase::getCreateDate);
        // 分页参数
        Page<CourseBase> page = new Page<>(params.getPageNo(), params.getPageSize());
        // 分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, wrapper);
        // 数据列表
        List<CourseBase> items = pageResult.getRecords();
        long total = pageResult.getTotal();
        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items, total, params.getPageNo(), params.getPageSize());
        return courseBasePageResult;
    }
}
