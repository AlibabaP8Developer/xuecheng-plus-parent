package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * <p>
 * 课程管理 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-02-06
 */
public interface CourseBaseInfoService extends IService<CourseBase> {

    /**
     * 课程查询
     * @param params 分页参数
     * @param paramsDto 查询条件
     * @return 结果
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto paramsDto);
}
