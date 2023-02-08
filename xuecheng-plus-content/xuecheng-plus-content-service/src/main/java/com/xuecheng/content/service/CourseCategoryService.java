package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

/**
 * <p>
 * 课程分类操作 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-02-08
 */
public interface CourseCategoryService extends IService<CourseCategory> {

    /**
     * 课程分类查询
     * @param id 根节点ID
     * @return 根节点下边的所有子结点
     */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);

}
