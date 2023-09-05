package com.festago.application;

import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class RedisServiceTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void name() {
        // given
        ListOperations<String, String> stringStringListOperations = stringRedisTemplate.opsForList();
        stringStringListOperations.rightPush("hello1", "world");
        stringStringListOperations.rightPush("hello2", "world");
        stringStringListOperations.rightPush("hello3", "world");
        stringStringListOperations.rightPush("hello4", "world");
        stringStringListOperations.rightPush("hello5", "world");
        stringStringListOperations.rightPush("hello6", "world");
        stringStringListOperations.rightPush("hello7", "world");

        // when
        // then
    }

    @Test
    void name2() {
        // given
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();

            Set<String> keys = stringRedisTemplate.keys("hello*");
            stringStringValueOperations.getAndExpire()

        System.out.println("keys.size() = " + keys.size());
        for (String key : keys) {
            System.out.println(key);
        }
        // when
        // then
    }
}
