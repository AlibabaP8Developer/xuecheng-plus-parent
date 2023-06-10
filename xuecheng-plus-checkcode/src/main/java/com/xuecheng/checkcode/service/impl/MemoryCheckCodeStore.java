package com.xuecheng.checkcode.service.impl;

import com.xuecheng.checkcode.service.CheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.M
 * @version 1.0
 * @description 使用本地内存存储验证码，测试用
 * @date 2022/9/29 18:36
 */
@Component("MemoryCheckCodeStore")
public class MemoryCheckCodeStore implements CheckCodeService.CheckCodeStore {

    Map<String,String> map = new HashMap<String,String>();

    public void set(String key, String value, Integer expire) {
        map.put(key,value);
    }

    public String get(String key) {
        return map.get(key);
    }

    public void remove(String key) {
        map.remove(key);
    }
}
