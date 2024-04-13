package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CoursePublishService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
public class CoursePublishController {

        @Autowired
        private CoursePublishService coursePublishService;

        @GetMapping("/coursepreview/{courseId}")
        public ModelAndView preview(@PathVariable("courseId") Long courseId){
                CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setViewName("course_template");
                modelAndView.addObject("model", coursePreviewInfo);
                return modelAndView;
        }

        @PostMapping("/courseaudit/commit/{courseId}")
        public void commitAudit(@PathVariable Long courseId) {
                Long companyId = 22L;
                coursePublishService.commitAudit(companyId, courseId);
        }

        @PostMapping("/coursepublish/{courseId}")
        public void coursePublish(@PathVariable Long courseId) {
                Long companyId = 22L;
                coursePublishService.publishCourse(companyId, courseId);
        }

        @ApiOperation("查询课程发布信息")
        @GetMapping("/r/coursepublish/{courseId}")
        public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId) {
                return coursePublishService.getCoursePublish(courseId);
        }
}
