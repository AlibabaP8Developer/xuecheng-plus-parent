package com.xuecheng.ucenter.client;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckCodeClientFactory implements FallbackFactory<CheckCodeClient> {

    @Override
    public CheckCodeClient create(Throwable throwable) {
        return (String key, String code) -> {
            log.debug("调用验证码服务熔断异常:{}", throwable.getMessage());
            return null;
        };
    }
}