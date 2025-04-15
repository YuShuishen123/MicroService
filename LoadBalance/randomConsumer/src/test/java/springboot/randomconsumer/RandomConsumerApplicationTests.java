package springboot.randomconsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RandomConsumerApplicationTests {
    private static final Logger logger = LogManager.getLogger(RandomConsumerApplicationTests.class);
    @Test
    void contextLoads() {
        logger.info("test");
    }

}
