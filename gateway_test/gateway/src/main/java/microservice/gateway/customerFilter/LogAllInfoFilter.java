package microservice.gateway.customerFilter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Yu'S'hui'shen
 */
@Slf4j
@Component
public class LogAllInfoFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 打印请求时间
        LocalDateTime requestTime = LocalDateTime.now();
        log.info("请求时间: {}", requestTime);

        // 打印请求URL
        String uri = request.getURI().toString();
        log.info("请求URL: {}", uri);

        // 打印请求头
        HttpHeaders headers = request.getHeaders();
        String headerInfo = headers.entrySet().stream()
               .map(entry -> entry.getKey() + ": " + entry.getValue())
               .collect(Collectors.joining(", "));
        log.info("请求头信息: {}", headerInfo);

        // 打印客户端IP，先尝试从X - Forwarded - For获取
        String clientIp = request.getHeaders().getFirst("X - Forwarded - For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        log.info("客户端IP: {}", clientIp);

        // 记录请求进入时间
        long start = System.currentTimeMillis();

        // 放行请求
        return chain.filter(exchange)
               .doOnTerminate(() -> {
                    // 比doFinally更早触发
                    HttpStatusCode code = response.getStatusCode();
                    log.info("最终状态码: {}", code);
                })
               .doFinally(signal -> {
                    long end = System.currentTimeMillis();
                    String status = signal.name();
                    log.info("耗时:{}ms, 终止信号:{}", end - start, status);
                });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
