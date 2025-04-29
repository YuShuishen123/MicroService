package microservice.feign_sentinel.consumer.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Configuration;


/**
 * @author Yu'S'hui'shen
 */

@Configuration
public class TimeTailLoadBalancerConfig {

    @Bean
    public ReactorServiceInstanceLoadBalancer timeTailLoadBalancer(LoadBalancerClientFactory factory) {
        String serviceId = "provider";
        ObjectProvider<ServiceInstanceListSupplier> provider =
                factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class);
        return new TimeTailLoadBalancer(serviceId, provider);
    }
}
