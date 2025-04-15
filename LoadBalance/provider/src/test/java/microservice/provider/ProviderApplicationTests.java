package microservice.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProviderApplicationTests {

    private static final Logger logger = LogManager.getLogger(ProviderApplicationTests.class);
    @Test
    void contextLoads() {
        logger.info("test");
    }

}
