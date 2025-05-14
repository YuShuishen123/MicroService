package microservice.micro_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 解密feiclient
 * @author Yu'S'hui'shen
 */
@FeignClient("gateway")
public interface DecryptClient {
    @PostMapping("/api/decrypt")
    public String decrypt(@RequestBody String mydate);
}

