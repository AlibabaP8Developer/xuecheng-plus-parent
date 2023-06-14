package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
            return "";
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
        UploadObjectArgs testbucket = null;
        try {
            testbucket = UploadObjectArgs.builder()
                    .bucket(bucket).object("第3章媒资管理模块v3.1.docx")
                    .object(objectName)// 对象名
                    .filename(localFilePath) // 指定本地文件路径
                    .contentType(mimeType)//默认根据扩展名确定文件内容类型，也可以指定
                    .build();

            // 上传文件
            minioClient.uploadObject(testbucket);
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
