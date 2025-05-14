package microservice.gateway.customerFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import microservice.gateway.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * 解密请求参数过滤器（完整修复版）
 * @author Yu'S'hui'shen
 */
@Component
public class DecryptParamGatewayFilterFactory extends AbstractGatewayFilterFactory<DecryptParamGatewayFilterFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(DecryptParamGatewayFilterFactory.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DecryptParamGatewayFilterFactory() {
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

            // 只处理POST请求且路径匹配/decrypt的请求
            if (!"POST".equalsIgnoreCase(request.getMethod().name()) ||
                    !request.getURI().getPath().contains("/decrypt")) {
                return chain.filter(exchange);
            }

            return DataBufferUtils.join(request.getBody())
                    .flatMap(dataBuffer -> {
                        try {
                            // 1. 读取原始请求体
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            String encryptedBody = new String(bytes, StandardCharsets.UTF_8);
                            log.info("原始加密请求体: {}", encryptedBody);

                            // 2. 执行解密
                            String decryptedBody = AESUtil.decrypt(encryptedBody, config.getKey());
                            log.info("解密结果: {}", decryptedBody);


                            // 4. 构建新的请求（关键修改部分）
                            ServerHttpRequestDecorator decoratedRequest = new ServerHttpRequestDecorator(request) {
                                @Override
                                public Flux<DataBuffer> getBody() {
                                    byte[] bodyBytes = decryptedBody.getBytes(StandardCharsets.UTF_8);
                                    return Flux.just(exchange.getResponse().bufferFactory().wrap(bodyBytes));
                                }

                                @Override
                                public HttpHeaders getHeaders() {
                                    HttpHeaders headers = new HttpHeaders();
                                    // 保留原始头（排除可能冲突的头）
                                    request.getHeaders().forEach((key, values) -> {
                                        if (!key.equalsIgnoreCase("Content-Length") &&
                                                !key.equalsIgnoreCase("Content-Type")) {
                                            headers.put(key, values);
                                        }
                                    });
                                    // 强制设置正确的Content-Type和Content-Length
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    headers.setContentLength(decryptedBody.getBytes(StandardCharsets.UTF_8).length);
                                    return headers;
                                }
                            };

                            // 5. 验证请求体是否可读（调试用）
                            decoratedRequest.getBody().collectList().block();

                            // 6. 转发修改后的请求
                            return chain.filter(exchange.mutate().request(decoratedRequest).build());

                        } catch (Exception e) {
                            log.error("7. 解密过程异常", e);
                            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                            return exchange.getResponse().writeWith(
                                    Flux.just(exchange.getResponse().bufferFactory()
                                            .wrap("解密失败".getBytes()))
                            );
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
