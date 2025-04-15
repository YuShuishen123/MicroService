package microservice.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConsumerApplicationTests {
    private static final Logger logger = LogManager.getLogger(ConsumerApplicationTests.class);
    @Test
    void contextLoads() {
        logger.info("test");
    }

}
