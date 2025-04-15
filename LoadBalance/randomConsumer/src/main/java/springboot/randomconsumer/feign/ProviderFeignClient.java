package springboot.randomconsumer.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Yu'S'hui'shen
 */
@FeignClient(name = "provider")
public interface ProviderFeignClient {

    /**
     *
     * @return 被调用服务的端口号
     */
    @GetMapping("/port")
    String getPort();

    /**
     *
     * @return 来自provider.yaml的配置文件的a值
     */
    @GetMapping("/a")
    String getA();
}
