package club.devhub.fleamarket.devhub.fleamarket;

import club.devhub.fleamarket.mapper.OrderMapper;
import club.devhub.fleamarket.mapper.ReportsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FleamarketApplicationTests {
    @Autowired
    private ReportsMapper reportsMapper;

    @Test
    void contextLoads() {
        System.out.println(reportsMapper.getList());
    }

}
