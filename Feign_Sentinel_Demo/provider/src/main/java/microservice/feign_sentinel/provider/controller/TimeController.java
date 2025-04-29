package microservice.feign_sentinel.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Yu'S'hui'shen
 * @date 2025/4/29
 * @description 时间服务提供者控制器
 */
@RestController
public class TimeController {

    @Value("${server.port}")
    private String serverPort;

    // 定义时间格式为HH:mm:ss（24小时制）
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 获取当前时间
     * @return 当前时间的时分秒(24小时制)，格式如："14:30:45"
     */
    @GetMapping("/provider/time")
    public String getTime() {
        // 获取端口号+当前时间并格式化为字符串
        return "端口号：" + this.serverPort + "，当前时间：" + LocalTime.now().format(TIME_FORMATTER);

    }
}
