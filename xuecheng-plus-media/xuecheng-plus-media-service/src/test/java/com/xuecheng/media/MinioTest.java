package com.xuecheng.media;

import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MinioTest {
    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://139.198.181.54:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void testUpload() {
        try {
            // 上传文件的参数信息
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("testbucket").object("第3章媒资管理模块v3.1.docx")
                    .object("/test/minio/第3章媒资管理模块v3.1.docx")// 对象名
                    .filename("/Users/lizhenghang/Desktop/学成在线项目—资料/day05 媒资管理 Nacos Gateway MinIO/资料/第3章媒资管理模块v3.1.docx") // 指定本地文件路径
                    //.contentType("video/mp4")//默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            // 上传文件
            minioClient.uploadObject(testbucket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDelete() {
        try {
            // 删除文件
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket("testbucket")  // 指定桶
                    .object("/Users/lizhenghang/Desktop/学成在线项目—资料/day05 媒资管理 Nacos Gateway MinIO/资料/第3章媒资管理模块v3.1.docx") // 指定删除的文件名
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
