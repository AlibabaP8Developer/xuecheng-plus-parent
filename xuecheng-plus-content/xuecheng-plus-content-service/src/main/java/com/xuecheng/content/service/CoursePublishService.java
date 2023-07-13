package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;

/**
 * 课程发布相关的接口
 */
public interface CoursePublishService {

    /**
     * 获取课程预览信息
     * @param courseId 课程ID
     * @return
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * @description 提交审核
     * @param courseId  课程id
     * @return void
     * @author Mr.M
     * @date 2022/9/18 10:31
     */
    public void commitAudit(Long companyId,Long courseId);

    /**
     * @description 课程发布接口
     * @param companyId 机构id
     * @param courseId 课程id
     * @return void
     * @author Mr.M
     * @date 2022/9/20 16:23
     */
    public void publish(Long companyId,Long courseId);
}
