package com.xuecheng.media;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BigFileTest {

    /**
     * 分块测试
     */
    @Test
    public void testChunk() throws Exception {
        // 源文件
        File sourceFile = new File("/Users/lizhenghang/Desktop/袁腾飞聊举报老师：连皇帝都怕这种人渣 #lifeano漫聊 230615.mp4");
        // 分块文件存储路径
        String chunkFilePath = "/Users/lizhenghang/workspace/java/itheima/video_chunk/";
        // 分块文件大小
        int chunkSize = 1024 * 1024 * 2;
        // 分块文件个数
        int chunkNum = (int) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        // 使用流从源文件读取数据，向分块文件中写数据
        RandomAccessFile raf_r = new RandomAccessFile(sourceFile, "r");
        // 缓冲区
        byte[] bytes = new byte[1024];
        for (int i = 0; i < chunkNum; i++) {
            File chunkFile = new File(chunkFilePath + i);
            // 分块文件写入流
            RandomAccessFile raf_rw = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = raf_r.read(bytes)) != -1) {
                raf_rw.write(bytes, 0, len);
                if (chunkFile.length() >= chunkSize) {
                    break;
                }
            }
            raf_rw.close();
        }
        raf_r.close();
    }

    /**
     * 将分块进行合并
     */
    @Test
    public void testMerge() throws Exception {
        // 块文件目录
        File chunkFolder = new File("/Users/lizhenghang/workspace/java/itheima/video_chunk/");

        // 合并后的文件
        File mergeFile = new File("/Users/lizhenghang/workspace/java/itheima/video_chunk/袁腾飞聊举报老师:连皇帝都怕这种人渣.mp4");

        // 取出所有的分块文件
        File[] files = chunkFolder.listFiles();
        // 将数组转成list
        List<File> filesList = Arrays.asList(files);
        // new Comparator<File》
        Collections.sort(filesList, (o1, o2) -> {
            return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
        });
        // 向合并文件写的流
        RandomAccessFile raf_rw = new RandomAccessFile(mergeFile, "rw");
        // 缓冲区
        byte[] bytes = new byte[1024];
        // 遍历分块文件，向合并的文件写
        for (File file : filesList) {
            // 读分块的流
            RandomAccessFile raf_r = new RandomAccessFile(file, "r");
            int len = -1;
            while ((len = raf_r.read(bytes)) != -1) {
                raf_rw.write(bytes, 0, len);
            }
            raf_r.close();
        }
        raf_rw.close();
    }

}
