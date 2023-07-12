package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/9/10 8:58
 */
@Service
@Slf4j
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    MediaFilesMapper mediaFilesMapper;
    @Autowired
    MinioClient minioClient;
    @Autowired
    MediaFileService currentProxy;

    // 存储普通文件
    @Value("${minio.bucket.files}")
    private String bucketMediaFiles;
    // 存储视频文件
    @Value("${minio.bucket.videofiles}")
    private String bucketVideofiles;

    /**
     * 根据媒资ID查询文件信息
     * @param mediaId
     * @return
     */
    @Override
    public MediaFiles getFileById(String mediaId) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(mediaId);
        return mediaFiles;
    }

    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<MediaFiles>();

        //分页对象
        Page<MediaFiles> page = new Page<MediaFiles>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<MediaFiles>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return mediaListResult;
    }

    /**
     * 将文件上传到minio
     *
     * @param companyId           机构ID
     * @param uploadFileParamsDto 文件信息
     * @param localFilePath       本地文件路径
     * @return
     */
    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath) {
        // 得到扩展名
        String filename = uploadFileParamsDto.getFilename();
        String extension = filename.substring(filename.lastIndexOf("."));

        String mimeType = getMimeType(extension);

        // 文件的默认目录 子目录
        String defaultFolderPath = getDefaultFolderPath();
        // 文件md5值
        String fileMd5 = getFileMd5(new File(localFilePath));
        String objectName = defaultFolderPath + fileMd5 + extension;
        // 上传文件到minio
        boolean result = addMediaFilesToMinIO(localFilePath, mimeType, bucketMediaFiles, objectName);
        if (!result) {
            XueChengPlusException.cast("上传文件失败");
        }
        // 入库信息
        // this：不是代理对象，而是原始对象，不能对事务进行控制，事务必须是代理对象才能生效
        MediaFiles mediaFiles = currentProxy.addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucketMediaFiles, objectName);
        if (mediaFiles == null) {
            XueChengPlusException.cast("文件上传后保存文件信息失败");
        }
        // 准备返回的对象
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);

        return uploadFileResultDto;
    }

    /**
     * @param companyId           机构id
     * @param fileMd5             文件md5值
     * @param uploadFileParamsDto 上传文件的信息
     * @param bucket              桶
     * @param objectName          对象名称
     * @return com.xuecheng.media.model.po.MediaFiles
     * @description 将文件信息添加到数据库文件表
     * @author Mr.M
     * @date 2022/10/12 21:22
     */
    @Transactional
    public MediaFiles addMediaFilesToDb(Long companyId, String fileMd5, UploadFileParamsDto uploadFileParamsDto, String bucket, String objectName) {
        //从数据库查询文件
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles == null) {
            mediaFiles = new MediaFiles();
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            // 文件ID
            mediaFiles.setId(fileMd5);
            // 机构ID
            mediaFiles.setCompanyId(companyId);
            // 桶
            mediaFiles.setBucket(bucketMediaFiles);
            // filePath
            mediaFiles.setFilePath(objectName);
            // file_id
            mediaFiles.setFileId(fileMd5);
            // url
            mediaFiles.setUrl("/" + bucket + "/" + objectName);
            // 上传时间
            mediaFiles.setCreateDate(LocalDateTime.now());
            // 状态 1：正常 0：不展示
            mediaFiles.setStatus("1");
            // 审核状态
            mediaFiles.setAuditStatus("002003");
            // 插入数据库
            int insert = this.mediaFilesMapper.insert(mediaFiles);
            if (insert < 0) {
                log.error("向数据库保存文件失败, bucket: {}, objectName: {}", bucket, objectName);
                return null;
            }
            return mediaFiles;
        }
        return mediaFiles;
    }

    /**
     * @param fileMd5 文件的md5
     * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
     * @description 检查文件是否存在
     * @author Mr.M
     * @date 2022/9/13 15:38
     */
    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        // 先查询数据库
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles != null) {
            // 桶
            String bucket = mediaFiles.getBucket();
            String filePath = mediaFiles.getFilePath();
            // 如果数据库存在再查询 minio
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucket)  // 指定桶
                    .object(filePath) // 指定删除的文件名
                    .build();
            try {
                // 查询远程服务获取到一个流对象
                FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
                if (inputStream != null) {
                    // 文件已经存在
                    return RestResponse.success(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 文件不存在
        return RestResponse.success(false);
    }

    /**
     * @param fileMd5    文件的md5
     * @param chunkIndex 分块序号
     * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
     * @description 检查分块是否存在
     * @author Mr.M
     * @date 2022/9/13 15:39
     */
    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {

        // 分块存储路径是：md5前两位为两个子目录，chunk存储分块文件
        // 根据md5得到分块文件所在目录的存储路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);

        // 如果数据库存在再查询 minio
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketVideofiles)  // 指定桶
                .object(chunkFileFolderPath + chunkIndex) // 指定删除的文件名
                .build();
        try {
            // 查询远程服务获取到一个流对象
            FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
            if (inputStream != null) {
                // 文件已经存在
                return RestResponse.success(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("错误消息：1::{}, 2::{}, 3::{}", e.getMessage(), e.getCause(), e.getLocalizedMessage());
        }
        // 文件不存在
        return RestResponse.success(false);
    }

    /**
     * @param fileMd5            文件md5
     * @param chunk              分块序号
     * @param localChunkFilePath 分块文件本地路径
     * @return com.xuecheng.base.model.RestResponse
     * @description 上传分块
     * @author Mr.M
     * @date 2022/9/13 15:50
     */
    @Override
    public RestResponse uploadChunk(String fileMd5, int chunk, String localChunkFilePath) {
        // 分块文件的路径
        String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunk;

        String mimeType = getMimeType(null);
        // 将分块文件上传到minio
        boolean b = addMediaFilesToMinIO(localChunkFilePath, mimeType, bucketVideofiles, chunkFilePath);
        if (!b) {
            log.debug("上传分块文件失败:{}", chunkFilePath);
            return RestResponse.validfail(false, "上传分块文件失败");
        }
        // 上传成功
        log.debug("上传分块文件成功:{}", chunkFilePath);
        return RestResponse.success(true);
    }

    /**
     * @param companyId           机构id
     * @param fileMd5             文件md5
     * @param chunkTotal          分块总和
     * @param uploadFileParamsDto 文件信息
     * @return com.xuecheng.base.model.RestResponse
     * @description 合并分块
     * @author Mr.M
     * @date 2022/9/13 15:56
     */
    @Override
    public RestResponse mergechunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        // 分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 找到分块文件调用minio的sdk进行文件合并
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i).limit(chunkTotal)
                .map(i -> ComposeSource.builder()
                        .bucket(bucketVideofiles)
                        .object(chunkFileFolderPath + i)
                        .build()
                )
                .collect(Collectors.toList());

        // 源文件名称
        String filename = uploadFileParamsDto.getFilename();
        // 扩展名
        String extenstion = filename.substring(filename.lastIndexOf("."));
        // 得到合并后的文件的地址
        String objectName = getFilePathByMd5(fileMd5, extenstion);

        // 合并后的objectName等信息
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket(bucketVideofiles)
                .object(objectName)
                .sources(sources) // 指定源文件
                .build();
        try {
            // =======合并文件=======
            minioClient.composeObject(composeObjectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("合并文件出错：bucket：{}, objectName：{}，错误信息：{}", bucketVideofiles, objectName, e.getMessage());
            return RestResponse.validfail(false, "合并文件出错");
        }
        // ==============校验合并后的和源文件是否一致，视频上传才成功==============
        // 先下载合并后的文件
        File file = downloadFileFromMinIO(bucketVideofiles, objectName);

        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            // 计算合并后的md5
            String mergeFileMd5 = DigestUtils.md5Hex(fileInputStream);
            // 比较原始md5和合并后的md5
            if (!fileMd5.equals(mergeFileMd5)) {
                log.error("校验合并失败md5值不一致，原始文件：{}， 合并文件: {}", fileMd5, mergeFileMd5);
                return RestResponse.validfail(false, "文件校验失败");
            }
            // 文件大小
            uploadFileParamsDto.setFileSize(file.length());
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail(false, "文件校验失败");
        }
        // ==============将文件信息入库==============
        MediaFiles mediaFiles = addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucketVideofiles, objectName);
        if (mediaFiles == null) {
            return RestResponse.validfail(false, "文件入库失败");
        }
        // 清理分块文件
        clearChunkFiles(chunkFileFolderPath, chunkTotal);
        return RestResponse.success(true);
    }

    /**
     * 清除分块文件
     *
     * @param chunkFileFolderPath 分块文件路径
     * @param chunkTotal          分块文件总数
     */
    private void clearChunkFiles(String chunkFileFolderPath, int chunkTotal) {
        try {
            Iterable<DeleteObject> objects = Stream.iterate(0, i -> ++i).limit(chunkTotal)
                    .map(i -> new DeleteObject(chunkFileFolderPath.concat(Integer.toString(i)))).collect(Collectors.toList());
            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket(bucketVideofiles).objects(objects).build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
            // 要想真正删除
            results.forEach(f->{
                try {
                    DeleteError deleteError = f.get();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从minio下载文件
     *
     * @param bucket     桶
     * @param objectName 对象名称
     * @return 下载后的文件
     */
    public File downloadFileFromMinIO(String bucket, String objectName) {
        //临时文件
        File minioFile = null;
        FileOutputStream outputStream = null;
        try {
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            //创建临时文件
            minioFile = File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minioFile);
            IOUtils.copy(stream, outputStream);
            return minioFile;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 得到合并后的文件的地址
     *
     * @param fileMd5 文件id即md5值
     * @param fileExt 文件扩展名
     * @return
     */
    private String getFilePathByMd5(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }

    /**
     * 得到分块文件的目录
     *
     * @param fileMd5
     * @return
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/chunk/";
    }

    /**
     * 获取文件默认存储目录路径 年/月/日
     *
     * @return
     */
    private String getDefaultFolderPath() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();

        String folder = year + "/" + month + "/" + day + "/";
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String folder = sdf.format(new Date()).replace("-", "/")+"/";
        return folder;
    }

    /**
     * 获取文件的md5
     *
     * @param file
     * @return
     */
    private String getFileMd5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String fileMd5 = DigestUtils.md5Hex(fileInputStream);
            return fileMd5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据扩展名获取mimeType
     */
    public String getMimeType(String extension) {
        if (StringUtils.isBlank(extension)) {
            extension = "";
        }
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用mimeType，字节流
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    /**
     * 将文件上传到minio
     *
     * @param localFilePath 文件本地路径
     * @param mimeType      媒体类型
     * @param bucket        桶
     * @param objectName    对象名 上传到minio上的路径
     * @return
     */
    public boolean addMediaFilesToMinIO(String localFilePath, String mimeType, String bucket, String objectName) {
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket) // 桶
                    .filename(localFilePath) // 指定本地文件路径
                    .object(objectName)// 对象名
                    .contentType(mimeType)//默认根据扩展名确定文件内容类型，也可以指定
                    .build();

            // 上传文件
            minioClient.uploadObject(uploadObjectArgs);
            log.debug("上传文件到minio成功,bucket:{}, mimeType：{}, localFilePath: {}, objectName: {}", bucket, mimeType, localFilePath, objectName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件出错,bucket:{}, mimeType：{}, localFilePath: {}, objectName: {}, 错误信息：{}",
                    bucket, mimeType, localFilePath, objectName, e.getMessage());
            return false;
        }
    }


}
