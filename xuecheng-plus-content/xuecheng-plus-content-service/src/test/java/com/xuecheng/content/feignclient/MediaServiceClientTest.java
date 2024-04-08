package com.xuecheng.content.feignclient;

import com.xuecheng.content.config.MultipartSupportConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MediaServiceClientTest {

    @Resource
    MediaServiceClient mediaServiceClient;

    //远程调用，上传文件
    @Test
    public void test() {
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(new File("D:\\test.html"));
        String result = mediaServiceClient.upload(multipartFile, "course", "test.html");
        System.out.println(result);
    }
}