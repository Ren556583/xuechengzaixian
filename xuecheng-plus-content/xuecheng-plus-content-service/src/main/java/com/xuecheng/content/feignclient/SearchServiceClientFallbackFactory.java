package com.xuecheng.content.feignclient;

import com.xuecheng.content.model.po.CourseIndex;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient> {
    @Override
    public SearchServiceClient create(Throwable throwable) {
        return new SearchServiceClient() {
            @Override
            public Boolean add(CourseIndex courseIndex) {
                log.error("添加课程索引发生熔断，索引信息：{}，熔断异常：{}",courseIndex,throwable.toString(),throwable);
                //走降级了返回false
                return false;
            }
        };
    }
}
