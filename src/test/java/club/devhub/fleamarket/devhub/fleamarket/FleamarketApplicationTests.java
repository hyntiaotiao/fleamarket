package club.devhub.fleamarket.devhub.fleamarket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootTest
public class FleamarketApplicationTests {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        //这里相当于redis对String类型的set操作
        redisTemplate.opsForValue().set("test", "helloworld");
        //这里相当于redis对String类型的get操作
        String test = (String) redisTemplate.opsForValue().get("test");
        System.out.println(test);
    }

}