package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;

import java.io.IOException;

/**
 * @author Mr.M
 * @version 1.0
 * @description 媒资文件管理业务类
 * @date 2022/9/10 8:55
 */
public interface MediaFileService {

    /**
     * @param pageParams          分页参数
     * @param queryMediaParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
     * @description 媒资文件查询方法
     * @date 2022/9/10 8:57
     */
    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

    /**
     * @description 上传文件的通用接口
     * @param companyId           机构id
     * @param uploadFileParamsDto 文件信息
     * @param bytes               文件字节数组
     * @param folder              桶下边的子目录
     * @param objectName          对象名称
     * @return com.xuecheng.media.model.dto.UploadFileResultDto
     */
    UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName);

    /**
     * 将文件信息添加到文件表
     *
     * @param companyId           机构id
     * @param uploadFileParamsDto 上传文件的信息
     * @param objectName          对象名称
     * @param fileMD5             文件md5码
     * @param bucket              桶
     * @return
     */
    MediaFiles addMediaFilesToDB(Long companyId, UploadFileParamsDto uploadFileParamsDto, String objectName, String fileMD5, String bucket);


    /**
     * 检查文件是否存在
     *
     * @param fileMd5 文件的md5
     * @return
     */
    RestResponse<Boolean> checkFile(String fileMd5);

    /**
     * 检查分块是否存在
     *
     * @param fileMd5    文件的MD5
     * @param chunkIndex 分块序号
     * @return
     */
    RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);

    /**
     * 上传分块
     * @param fileMd5   文件MD5
     * @param chunk     分块序号
     * @param bytes     文件字节
     * @return
     */
    RestResponse uploadChunk(String fileMd5,int chunk,byte[] bytes);

    /**
     * 合并分块
     *
     * @param companyId           机构id
     * @param fileMd5             文件MD5
     * @param chunkTotal          分块数量
     * @param uploadFileParamsDto 文件信息
     * @return
     */
    RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) throws IOException;

    /**
     * 根据id查询媒资文件
     * @param mediaId
     * @return
     */
    MediaFiles getFileById(String mediaId);
}
