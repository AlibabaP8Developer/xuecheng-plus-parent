package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-02-06
 */
public interface TeachplanService extends IService<Teachplan> {

    /**
     * 根据课程ID查询课程计划
     * @param courseId
     * @return
     */
    List<TeachplanDto> findTeachplayTree(Long courseId);

    /**
     * 保存课程计划
     * @param saveTeachplanDto
     */
    void saveTeachplan(SaveTeachplanDto saveTeachplanDto);
}
