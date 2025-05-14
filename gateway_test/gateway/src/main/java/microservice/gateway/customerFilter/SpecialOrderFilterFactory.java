package microservice.gateway.customerFilter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Yu'S'hui'shen
 */
@Slf4j
@Component
public class SpecialOrderFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    @Autowired
    public SpecialOrderFilterFactory(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            boolean isSpecialPath = "/orderspecial".equalsIgnoreCase(path);
            boolean hasNameCookie = request.getCookies().containsKey("name");

            if (isSpecialPath) {
                if (hasNameCookie) {
                    log.info("触发特殊订单提示，转发到 http://127.0.0.1:7072");

                    return webClient
                            .method(request.getMethod())
                            .uri("http://127.0.0.1:7072" + path)
                            .headers(headers -> headers.addAll(request.getHeaders()))
                            .retrieve()
                            .toEntity(byte[].class)
                            .flatMap(responseEntity -> writeResponse(exchange, responseEntity));
                } else {
                    log.info("Cookie 不存在，重写路径为 /getall");

                    ServerHttpRequest mutatedRequest = request.mutate()
                            .path("/getall")
                            .build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, ResponseEntity<byte[]> responseEntity) {
        var response = exchange.getResponse();
        response.setStatusCode(responseEntity.getStatusCode());
        HttpHeaders headers = responseEntity.getHeaders();
        headers.forEach((key, values) -> response.getHeaders().put(key, values));
        if (responseEntity.getBody() != null) {
            return response.writeWith(Mono.just(response.bufferFactory().wrap(responseEntity.getBody())));
        }
        return response.setComplete();
    }
}
