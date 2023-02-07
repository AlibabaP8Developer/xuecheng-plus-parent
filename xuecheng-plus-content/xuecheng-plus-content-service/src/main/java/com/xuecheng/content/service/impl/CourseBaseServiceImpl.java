package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.String;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
