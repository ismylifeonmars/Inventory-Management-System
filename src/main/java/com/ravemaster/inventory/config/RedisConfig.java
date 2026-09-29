package com.ravemaster.inventory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ravemaster.inventory.domain.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<ProductDto> serializer =
                new Jackson2JsonRedisSerializer<>(mapper, ProductDto.class);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer));

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
//for spring boot 4 and later
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
//                                          JsonMapper jsonMapper) {
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10))
//                .disableCachingNullValues()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                        .fromSerializer(new JacksonJsonRedisSerializer<>(jsonMapper, ProductDto.class)));
//
//        return RedisCacheManager
//                .builder(connectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .build();
//    }

}
