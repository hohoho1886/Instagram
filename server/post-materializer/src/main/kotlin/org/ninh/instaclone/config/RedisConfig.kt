package org.ninh.instaclone.config

import org.ninh.instaclone.dto.UploadMessage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import tools.jackson.databind.ObjectMapper


@Configuration
class RedisConfig {
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory?): RedisTemplate<String, UploadMessage> {
        val redisTemplate: RedisTemplate<String, UploadMessage> = RedisTemplate()
        redisTemplate.connectionFactory = connectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()

        redisTemplate.valueSerializer = GenericJacksonJsonRedisSerializer(ObjectMapper())

        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }
}