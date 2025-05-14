package microservice.micro_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Yu'S'hui'shen
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }

}
