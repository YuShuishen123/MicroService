package springboot.randomconsumer.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * ProjectName:    springcloudalibaba
 * ClassName:    DepartConfig
 * Package:    com.cheese.config
 * Description: 用于修改负载均衡策略
 * Datetime:    2024-07-30   1:21
 * @author LJZ
 */
public class DepartConfig {
    @Bean
    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                                   LoadBalancerClientFactory factory) {
        //获取负载均衡客户端名称,即提供者服务名称
        String serverName = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        //获取提供者微服务名称可用的实例列表
        ObjectProvider<ServiceInstanceListSupplier> lazyProvider = factory.getLazyProvider(serverName, ServiceInstanceListSupplier.class);
        //从集合lazyProvider中获取指定serverName的实例,随机做负载均衡
        return new RandomLoadBalancer(lazyProvider, serverName);
    }
}
