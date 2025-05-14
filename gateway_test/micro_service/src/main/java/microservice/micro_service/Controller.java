package microservice.micro_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import microservice.micro_service.feignClient.DecryptClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yu'S'hui'shen
 * @date 2025/5/13
 * @description 测试微服务
 */
@RestController
public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    // 引入配置文件当中的变量
    @Value("${server.port}")
    String port;
    @Value("${spring.application.name}")
    String serviceName;

    @Resource
    DecryptClient decryptClient;

    public static final String REPEATABLE_STATEMENT = "响应微服务名称: ";


    // 返回服务名+端口号+接口等级url
    @GetMapping("/getall")
    public String getUrl(HttpServletRequest request) {
        return REPEATABLE_STATEMENT+serviceName + ":" + port + ",请求路径: " + request.getRequestURL();
    }

    // 作业2:特殊订单接口,7072负责
    @GetMapping("/orderspecial")
    public String orderSpecial(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        StringBuilder cookieDetails = new StringBuilder();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieDetails.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
            }
        } else {
            cookieDetails.append("No cookies found");
        }
        log.info("接收到特殊订单请求: {}", cookieDetails);
        return "特殊订单接口响应: " + serviceName + ":" + port + ", cookie: " + cookieDetails + ", 请求路径: " + request.getRequestURL();
    }

    // 作业4:url隐藏接口,对应,使用7074负责
    @GetMapping("/x/y/test1")
    public String test1(HttpServletRequest request) {
        return REPEATABLE_STATEMENT +serviceName + ":" + port +
                "实现url隐藏测试接口,最终请求路径: "+request.getRequestURL();
    }

    // 作业5:接收加密后的mydate参数,类型为json字符串,使用7074负责
    @PostMapping("/encrypt")
    public String encrypt(@RequestBody String mydata) {
        log.info("接收到加密后的mydate参数: {}", mydata);
        return decryptClient.decrypt(mydata);
    }

    // 接收解密后的mydate参数,类型为json字符串,使用7074负责
    @PostMapping("/decrypt")
    public String decrypt(@RequestBody String mydata) {
        log.info("接收到解密后的mydate参数: {}", mydata);
        try {
            JsonNode node = new ObjectMapper().readTree(mydata);
            return "接收到解密后的mydate参数: " + node.toString();
        } catch (Exception e) {
            return "数据格式错误: " + mydata;
        }
    }

}
