package com.xuecheng.media.model.dto;

import lombok.Data;

/**
 * @author Mr.M
 * @version 1.0
 * @description 上传普通文件请求参数-文件信息
 * @date 2022/9/12 18:49
 */
@Data
public class UploadFileParamsDto {
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 文件类型（文档，音频，视频）
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 标签
     */
    private String tags;

    /**
     * 上传人
     */
    private String username;

    /**
     * 备注
     */
    private String remark;
}
