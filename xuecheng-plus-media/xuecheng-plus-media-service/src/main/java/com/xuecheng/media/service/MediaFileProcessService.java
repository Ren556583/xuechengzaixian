package com.xuecheng.media.service;

import com.xuecheng.media.model.po.MediaProcess;

import java.util.List;

public interface MediaFileProcessService {
    /**
     * 获取待处理任务
     * @param shardIndex    分片序号
     * @param shardTotal    分片总数
     * @param count         获取记录数
     * @return  待处理任务集合
     */
    List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count);

    /**
     * 任务处理完成后，更新任务处理结果
     * @param taskId
     * @param status
     * @param fileId
     * @param url
     * @param errorMsg
     */
    void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);
}
