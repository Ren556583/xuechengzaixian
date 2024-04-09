package com.xuecheng.content.service.jobhandler;

import com.xuecheng.content.feignclient.SearchServiceClient;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.model.po.CourseIndex;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.exception.XueChengPlusException;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {

    @Autowired
    private CoursePublishService coursePublishService;

    @Autowired
    CoursePublishMapper coursePublishMapper;

    @Autowired
    SearchServiceClient searchServiceClient;

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
        saveCourseIndex(mqMessage, Long.valueOf(courseId));
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

    public void saveCourseIndex(MqMessage mqMessage, Long courseId) {
        //任务id
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        //取出第二个阶段状态
        int stageTwo = mqMessageService.getStageTwo(taskId);

        //任务幂等处理
        if (stageTwo>0){
            log.debug("课程索引已写入，无需执行。。。");
            return;
        }
        //查询课程信息，调用搜索服务添加索引接口
        //从课程发布表查询课程信息
        CoursePublish coursePublish = coursePublishMapper.selectById(courseId);
        CourseIndex courseIndex = new CourseIndex();
        BeanUtils.copyProperties(coursePublish,courseIndex);

        //远程调用
        Boolean add = searchServiceClient.add(courseIndex);
        if (!add) {
            XueChengPlusException.cast("远程调用搜索服务添加课程索引失败");
        }

        mqMessageService.completedStageTwo(taskId);
    }
}
