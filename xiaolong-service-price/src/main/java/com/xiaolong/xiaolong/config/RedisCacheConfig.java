package com.xiaolong.xiaolong.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/*
 * Redis过期时间配置类
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 创建 JSON 序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置 Key 的序列化器为 String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(jsonSerializer);
        // 设置 Value 的序列化器为 JSON
        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);
        return redisTemplate;
    }

//    @Bean
//    public RedisSerializer<Object> redisSerializer() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.activateDefaultTyping(
//                LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//        return new GenericJackson2JsonRedisSerializer(objectMapper);
//    }

//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
//        // 创建默认配置
//        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .disableCachingNullValues(); // 不缓存null值
//
//        // 为不同的缓存名称设置不同的过期时间
//        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
//        // 当前价格缓存5分钟
//        configMap.put("now", defaultConfig.entryTtl(Duration.ofMinutes(5)));
//        // 查询一天价格缓存15分钟
//        configMap.put("day", defaultConfig.entryTtl(Duration.ofMinutes(15)));
//        // 查询一周价格缓存2小时
//        configMap.put("week", defaultConfig.entryTtl(Duration.ofHours(2)));
//        // 查询一月价格缓存6小时
//        configMap.put("month", defaultConfig.entryTtl(Duration.ofHours(6)));
//        // 查询三月价格缓存1天
//        configMap.put("3month", defaultConfig.entryTtl(Duration.ofDays(1)));
//
//        return RedisCacheManager.builder(factory)
//                .cacheDefaults(defaultConfig)
//                .withInitialCacheConfigurations(configMap)
//                .build();
//    }
}
