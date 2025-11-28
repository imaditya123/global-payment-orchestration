package com.global.apigateway.config;


import com.global.apigateway.dto.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.time.Instant;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    public Mono<Void> handleResponseStatusException(ServerWebExchange exchange, ResponseStatusException ex) {

        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }



        ErrorResponse err = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getReason())
                .path(exchange.getRequest().getPath().value())
                .build();



        exchange.getResponse().setStatusCode(ex.getStatusCode());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);


        byte[] bytes = err.toJsonBytes();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }


    @ExceptionHandler(Exception.class)
    public Mono<Void> handleGeneric(ServerWebExchange exchange, Exception ex) {
        ErrorResponse err = ErrorResponse.builder()
                                        .timestamp(Instant.now().toString())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                        .message(ex.getMessage())
                                        .path(exchange.getRequest().getPath().value())
                                        .build();


        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = err.toJsonBytes();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}