package microservice.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Yu'S'hui'shen
 */

/*此条配置可以开启随机负载均衡*/
//@LoadBalancerClients(defaultConfiguration = DepartConfig.class)

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
