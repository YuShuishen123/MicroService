package microservice.feign_sentinel.consumer.controller;

import microservice.feign_sentinel.consumer.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yu'S'hui'shen
 * @date 2025/4/29
 * @description
 */
@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/consumer/time")
    public String getTime(Long id) {
        return testService.getTime(id);
    }

}
