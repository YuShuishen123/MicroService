package microservice.feign_sentinel.consumer.service;

import MicroService.Feign_Sentinel.feign_api.feignClient.TestFeignClient;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

/**
 * @author Yu'S'hui'shen
 * @date 2025/4/29
 * @description
 */
@Service
public class TestService {

    private final TestFeignClient testFeignClient;

    public TestService(TestFeignClient testFeignClient) {
        this.testFeignClient = testFeignClient;
    }

    @SentinelResource(
            value = "getTime",
            fallback = "getTimeFallback",    // 业务异常的兜底方法
            blockHandler = "getTimeBlockHandler"  // 流控的兜底方法
    )
    public String getTime(Long id) {
        return testFeignClient.getTime(id);
    }

    // 业务异常时的兜底方法
    public String getTimeFallback(Long id,Throwable ex) {
        // 这里可以记录日志或者返回默认值
        return "Fallback: 业务抛出异常";
    }

    // 流控时的兜底方法
    public String getTimeBlockHandler(Long id,BlockException ex) {
        // 这里可以记录日志或返回其他处理方式
        return "BlockHandler: 触发流控规则";
    }

}
