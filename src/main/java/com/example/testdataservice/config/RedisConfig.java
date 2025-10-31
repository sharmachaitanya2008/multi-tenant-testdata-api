package com.example.testdataservice.config;
import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*; import org.springframework.data.redis.connection.lettuce.*; import org.springframework.data.redis.core.*;
@Configuration
public class RedisConfig {
    @Bean public RedisConnectionFactory redisConnectionFactory() { return new LettuceConnectionFactory(); }
    @Bean public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>(); template.setConnectionFactory(factory); template.afterPropertiesSet(); return template;
    }
}
