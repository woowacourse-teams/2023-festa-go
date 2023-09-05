package com.festago.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    StringRedisTemplate redisTemplate;

    public void test() {
    }

}
