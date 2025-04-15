package microservice.consumer.configuration;


import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yu'S'hui'shen
 */
@Configuration
public class ConfigApplication {
    @Bean
    Logger.Level feignLoggerLeve() {
        return Logger.Level.BASIC;
    }
}
