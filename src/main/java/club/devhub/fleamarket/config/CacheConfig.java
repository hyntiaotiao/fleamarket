package club.devhub.fleamarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存配置类（后端开发初级篇不能讲Redis，只好拿ConcurrentHashMap凑活用）
 */
@Configuration
public class CacheConfig {

    /**
     * 往IOC容器中注入一个ConcurrentHashMap<String,String>
     */
    @Bean
    public ConcurrentHashMap<String, String> concurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

}
