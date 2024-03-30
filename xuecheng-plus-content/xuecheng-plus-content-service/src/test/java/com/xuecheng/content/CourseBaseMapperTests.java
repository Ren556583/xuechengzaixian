package com.xuecheng.content;

import com.xuecheng.ContentApplication;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.po.CourseBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ContentApplication.class)
@Slf4j
public class CourseBaseMapperTests {
    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Test
    public void contextLoads() {
        CourseBase courseBase = courseBaseMapper.selectById(18);
        log.info("查询到数据：{}", courseBase);
        Assertions.assertNotNull(courseBase);
    }
}
