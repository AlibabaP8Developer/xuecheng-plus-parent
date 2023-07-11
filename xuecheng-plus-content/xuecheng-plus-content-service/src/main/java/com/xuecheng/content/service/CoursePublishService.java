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
}
