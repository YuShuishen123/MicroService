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


    ProviderFeignClient providerFeignClient;
    public ConsumerController(ProviderFeignClient providerFeignClient) {
        this.providerFeignClient = providerFeignClient;
    }

    @Value("${a}")
    private String a;
    @GetMapping("/port")
    public String getPort(){
        return providerFeignClient.getPort();
    }

    @GetMapping("/a")
    public String getA(){
        return providerFeignClient.getA() + "," + "the a from consumer:" + a;
    }



}
