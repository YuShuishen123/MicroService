package microservice.feign_sentinel.consumer.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yu'S'hui'shen
 * @date 2025/4/29
 * @description
 */
@Configuration
@LoadBalancerClient(name = "provider", configuration = TimeTailLoadBalancerConfig.class)
public class ProviderLoadBalancerClientConfig {
}

