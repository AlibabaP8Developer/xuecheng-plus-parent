package com.xuecheng.content.model.dto;

import com.xuecheng.base.exception.ValidationGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Mr.M
 * @version 1.0
 * @description 添加课程dto
 * @date 2022/9/7 17:40
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCourseDto extends AddCourseDto {

    // 课程ID
    private Long id;
}
