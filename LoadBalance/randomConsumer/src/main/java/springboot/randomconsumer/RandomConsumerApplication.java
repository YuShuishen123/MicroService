package springboot.randomconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springboot.randomconsumer.configuration.DepartConfig;

/**
 * @author Yu'S'hui'shen
 */
@SpringBootApplication
@EnableFeignClients
@LoadBalancerClients(defaultConfiguration = DepartConfig.class)
public class RandomConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RandomConsumerApplication.class, args);
    }

}
