package microservice.consumer.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Yu'S'hui'shen
 */
@FeignClient(name = "provider")
public interface ProviderFeignClient {

    @GetMapping("/a")
    public String getA();
}
