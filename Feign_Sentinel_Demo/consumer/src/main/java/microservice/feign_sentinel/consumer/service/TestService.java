package microservice.feign_sentinel.consumer.service;

import MicroService.Feign_Sentinel.feign_api.feignClient.TestFeignClient;
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

    public String getTime(){
        return testFeignClient.getTime();
    }

}
