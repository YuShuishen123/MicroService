package microservice.consumer;


import microservice.consumer.feign.ProviderFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yu'S'hui'shen
 */
@RestController
@RefreshScope
public class ConsumerController {

    @Value("${a}")
    private String a;

    ProviderFeignClient providerFeignClient;
    public ConsumerController(ProviderFeignClient providerFeignClient) {
        this.providerFeignClient = providerFeignClient;
    }
    @GetMapping("/test")
    public String getAll(){
        return providerFeignClient.getA() + "," + a;
    }
}
