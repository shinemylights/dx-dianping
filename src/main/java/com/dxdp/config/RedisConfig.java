package com.dxdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement // 1
public class RedisConfig {
    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        // 配置redisTemplate
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        // 打开事务支持
        stringRedisTemplate.setEnableTransactionSupport(true); // 2
        return stringRedisTemplate;
    }

    // @Bean
    // public PlatformTransactionManager transactionManager() throws SQLException {
    //     return new DataSourceTransactionManager(dataSource()); // 3
    // }
    //
    // @Bean
    // public DataSource dataSource() throws SQLException {
    //     // ...
    // }
}

