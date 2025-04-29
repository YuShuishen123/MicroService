package MicroService.Feign_Sentinel.feign_api.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author Yu'S'hui'shen
 */
@FeignClient(name = "provider")
public interface TestFeignClient {
    @GetMapping("/provider/time")
    String getTime();
}
