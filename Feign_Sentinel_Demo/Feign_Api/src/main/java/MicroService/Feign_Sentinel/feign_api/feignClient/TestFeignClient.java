package MicroService.Feign_Sentinel.feign_api.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author Yu'S'hui'shen
 */
@FeignClient(name = "provider")
public interface TestFeignClient {
    @GetMapping("/provider/time")
    String getTime(@RequestParam Long id);  // 确保参数作为 Query 传递
}

