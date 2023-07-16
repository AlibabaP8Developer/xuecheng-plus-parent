package com.xuecheng.learning.feignclient;

import com.xuecheng.content.model.po.CoursePublish;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/10/25 9:14
 */
@Slf4j
@Component
public class ContentServiceClientFallbackFactory implements FallbackFactory<ContentServiceClient> {
    public ContentServiceClient create(final Throwable throwable) {
        return new ContentServiceClient() {

            public CoursePublish getCoursepublish(Long courseId) {
                log.error("调用内容管理服务发生熔断:{}", throwable.toString(),throwable);
                return null;
            }
        };
    }
}
