package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

@Data
public class TeachplanDto extends Teachplan {

    /**
     * 子课程目录
     */
    private List<TeachplanDto> teachPlanTreeNodes;

    /**
     * 关联的媒资信息
     */
    TeachplanMedia teachplanMedia;
}
