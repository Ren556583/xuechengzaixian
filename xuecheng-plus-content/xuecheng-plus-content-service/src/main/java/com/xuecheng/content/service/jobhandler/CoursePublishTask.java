package com.xuecheng.content.service.jobhandler;

import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.exception.XueChengPlusException;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {

    @Autowired
    private CoursePublishService coursePublishService;

    @XxlJob("CoursePublishJobHandler")
    private void coursePublishJobHandler() {
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        process(shardIndex, shardTotal, "course_publish", 5, 60);
        log.debug("测试任务执行中...");
    }

    @Override
    public boolean execute(MqMessage mqMessage) {
        log.debug("开始执行课程发布任务，课程id：{}", mqMessage.getBusinessKey1());
        // TODO 将课程信息静态页面上传至MinIO
        String courseId = mqMessage.getBusinessKey1();
        generateCourseHtml(mqMessage, Long.valueOf(courseId));
        // TODO 存储到Redis

        // TODO 存储到ElasticSearch
        return true;
    }

    private void generateCourseHtml(MqMessage mqMessage, Long courseId) {
        // 1. 幂等性判断
        // 1.1 获取消息id
        Long id = mqMessage.getId();
        // 1.2 获取小任务阶段状态
        MqMessageService mqMessageService = this.getMqMessageService();
        int stageOne = mqMessageService.getStageOne(id);
        // 1.3 判断小任务阶段是否完成
        if (stageOne == 1) {
            log.debug("当前阶段为静态化课程信息任务，已完成，无需再次处理，任务信息：{}", mqMessage);
            return;
        }
        // 2. 生成静态页面
        File file = coursePublishService.generateCourseHtml(Long.valueOf(courseId));
        if (file == null) {
            XueChengPlusException.cast("课程静态化异常");
        }
        // 3. 将静态页面上传至MinIO
        coursePublishService.uploadCourseHtml(Long.valueOf(courseId), file);
        // 4. 保存第一阶段状态
        mqMessageService.completedStageOne(id);
    }
}
