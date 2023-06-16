package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinioTest {
    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://139.198.181.54:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void testUpload() {
        try {
            // 通过扩展名得到媒体资源类型 mimeType
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".mp4");
            String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用mimeType，字节流
            if (extensionMatch != null) {
                mimeType = extensionMatch.getMimeType();
            }

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

    @Test
    public void testGetFile() {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket("testbucket")  // 指定桶
                    .object("/test/minio/第3章媒资管理模块v3.1.docx") // 指定删除的文件名
                    .build();
            FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
            // 指定输出流
            FileOutputStream outputStream = new FileOutputStream(new File("/Users/lizhenghang/Desktop/1.docx"));
            IOUtils.copy(inputStream, outputStream);
            // 校验文件的完整性对文件的内容进行md5
            String sourceMd5 = DigestUtils.md5Hex(inputStream); // minio中文件md5值
            String localMd5 = DigestUtils.md5Hex(new FileInputStream(new File("/Users/lizhenghang/Desktop/1.docx")));
            if (sourceMd5.equals(localMd5)) {
                System.out.println("下载成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将分块文件上传到minio
     */
    @Test
    public void uploadChunk() throws Exception {
        for (int i = 0; i < 12; i++) {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("testbucket")
                    .object("/chunk/minio/" + i)// 对象名 minio中的路径和 文件名
                    .filename("/Users/lizhenghang/workspace/java/itheima/video_chunk/" + i) // 指定本地文件路径
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传" + i + "成功");
        }
    }

    /**
     * 调用minio接口合并分块
     */
    @Test
    public void testMerge() throws Exception {
        //List<ComposeSource> sources = new ArrayList<>();
        //for (int i = 0; i < 56; i++) {
        //    // 指定分块的文件信息
        //    ComposeSource composeSource = ComposeSource.builder()
        //            .bucket("testbucket")
        //            .object("chunk/minio/" + i)
        //            .build();
        //    sources.add(composeSource);
        //}

        List<ComposeSource> sources = Stream.iterate(0, i -> ++i).limit(12)
                .map(i -> ComposeSource.builder()
                        .bucket("testbucket")
                        .object("/chunk/minio/" + i)
                        .build()
                )
                .collect(Collectors.toList());

        // 合并后的objectName等信息
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket("testbucket")
                .object("袁腾飞聊举报老师：连皇帝都怕这种人渣.mp4")
                .sources(sources) // 指定源文件
                .build();
        // 合并文件
        minioClient.composeObject(composeObjectArgs);

        /*
            java.lang.IllegalArgumentException: source testbucket/chunk/minio/0: size 1048576 must be greater than 5242880
            报错原因：默认的分块大小为5M
         */
    }

    /**
     * 批量清理分块文件
     */


}
