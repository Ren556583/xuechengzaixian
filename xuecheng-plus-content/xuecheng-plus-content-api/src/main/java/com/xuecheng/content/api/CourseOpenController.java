package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.service.CoursePublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/open")
public class CourseOpenController {
    @Autowired
    private CoursePublishService coursePublishService;

    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getPreviewInfo(@PathVariable Long courseId){
        // 获取课程预览信息
        return coursePublishService.getCoursePreviewInfo(courseId);
    }
}
