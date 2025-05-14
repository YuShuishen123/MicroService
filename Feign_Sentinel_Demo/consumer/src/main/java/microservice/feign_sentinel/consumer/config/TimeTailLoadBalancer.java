package microservice.feign_sentinel.consumer.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.ObjectProvider;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Yu'S'hui'shen
 */
public class TimeTailLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public TimeTailLoadBalancer(String serviceId, ObjectProvider<ServiceInstanceListSupplier> provider) {
        this.serviceInstanceListSupplierProvider = provider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(org.springframework.cloud.client.loadbalancer.Request request) {
        return Objects.requireNonNull(serviceInstanceListSupplierProvider.getIfAvailable()).get().next().map(serviceInstances -> {
            if (serviceInstances.isEmpty()) {
                return new EmptyResponse();
            }

            int second = LocalDateTime.now().getSecond();
            int tail = second % 10;
            if (tail == 0) {
                tail = 1;
            }
            int index = tail % 3;

            if (index >= serviceInstances.size()) {
                index = index % serviceInstances.size();
            }

            ServiceInstance instance = serviceInstances.get(index);
            return new DefaultResponse(instance);
        });
    }
}
