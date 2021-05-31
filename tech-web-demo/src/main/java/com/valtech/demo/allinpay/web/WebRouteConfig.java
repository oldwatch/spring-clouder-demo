package com.valtech.demo.allinpay.web;

import com.valtech.demo.allinpay.service.PaymentService;
import com.valtech.demo.allinpay.vo.SubmitOrderInput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebRouteConfig {

    private final PaymentService paymentService;

    private final WebClient client;


    public WebRouteConfig(PaymentService paymentService) {
        this.paymentService = paymentService;
        client = WebClient.create("https://cn.bing.com");
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(POST("/payment/submitWithPay"), this::submitAndPay)
                .andRoute(GET("/hello"), this::hello)
                .andRoute(GET("/proxy/*"), this::proxy)
                ;
    }

    private Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().bodyValue("hello");
    }


    private Mono<ServerResponse> proxy(ServerRequest request) {

        return client
                .get()
                .uri((builder) -> {
                    URI targetUri = request.uri();
                    String subPath = StringUtils.substringAfter(targetUri.getPath(), "/proxy");
                    builder.path(subPath).queryParams(request.queryParams());
                    return builder.build();
                })
                .exchange()
                .flatMap(clientResp -> {
                    return clientResp.body((resp, context) -> {
                        return ServerResponse.status(resp.getStatusCode())
                                .headers((headers) -> headers.addAll(resp.getHeaders()))
                                .body(resp.getBody(), DataBuffer.class);
                    });
                });

    }

    private Mono<ServerResponse> submitAndPay(ServerRequest request) {
        return request.bodyToMono(SubmitOrderInput.class)
                .map(vo -> vo.generPayRequest())
                .flatMap(param -> paymentService.payUrl(param))
                .flatMap(url -> ServerResponse
                        .seeOther(url)
                        .build());
    }


}
