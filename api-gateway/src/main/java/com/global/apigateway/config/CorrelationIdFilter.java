package com.global.apigateway.config;


import com.global.apigateway.util.CorrelationIdUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Component
public class CorrelationIdFilter implements WebFilter {
    private static final String HEADER = "X-Correlation-Id";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String id = request.getHeaders().getFirst(HEADER);
        String corrId = (id == null || id.isBlank())
                ? CorrelationIdUtil.generateCorrelationId()
                : id;



        ServerWebExchange mutated = exchange.mutate()
                                    .request(request.mutate().header(HEADER, corrId).build())
                                    .build();


        return chain.filter(mutated)
                .contextWrite(ctx -> ctx.put(HEADER, corrId));
    }
}