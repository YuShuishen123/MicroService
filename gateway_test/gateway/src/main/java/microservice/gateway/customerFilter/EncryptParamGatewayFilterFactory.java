package microservice.gateway.customerFilter;

import lombok.Getter;
import lombok.Setter;
import microservice.gateway.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @author Yu'S'hui'shen
 */
@Component
public class EncryptParamGatewayFilterFactory extends AbstractGatewayFilterFactory<EncryptParamGatewayFilterFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(EncryptParamGatewayFilterFactory.class);

    public EncryptParamGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("paramName");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!"POST".equalsIgnoreCase(request.getMethod().name())) {
                return chain.filter(exchange);
            }

            return DataBufferUtils.join(request.getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);

                        String requestBody = new String(bytes, StandardCharsets.UTF_8);
                        log.info("原始请求体: {}", requestBody);

                        try {
                            String encryptedBody = AESUtil.encrypt(requestBody, config.getKey());
                            log.info("加密后请求体: {}", encryptedBody);

                            byte[] newBytes = encryptedBody.getBytes(StandardCharsets.UTF_8);
                            DataBuffer newDataBuffer = exchange.getResponse().bufferFactory().wrap(newBytes);

                            // 创建装饰器来替换请求体
                            ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(request) {
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    return Flux.just(newDataBuffer);
                                }
                            };

                            // 更新Content-Length头
                            ServerHttpRequest newRequest = decoratedRequest.mutate()
                                    .header("Content-Length", String.valueOf(newBytes.length))
                                    .build();

                            return chain.filter(exchange.mutate().request(newRequest).build());
                        } catch (Exception e) {
                            log.error("请求体加密失败: {}", e.getMessage());
                            return Mono.error(e);
                        }
                    });
        };
    }



    @Setter
    @Getter
    public static class Config {
        private String key = "defaultEncryptionKey";
    }
}
