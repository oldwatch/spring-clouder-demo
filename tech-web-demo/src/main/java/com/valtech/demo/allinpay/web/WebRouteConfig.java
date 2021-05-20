package com.valtech.demo.allinpay.web;

import com.valtech.demo.allinpay.service.PaymentService;
import com.valtech.demo.allinpay.vo.SubmitOrderInput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebRouteConfig {

    private final PaymentService paymentService;

    public WebRouteConfig(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(POST("/payment/submitWithPay"), this::submitAndPay)
                ;
    }

    private Mono<ServerResponse> submitAndPay(ServerRequest request) {
        return request.bodyToMono(SubmitOrderInput.class)
                .map(vo -> vo.generPayRequest())
                .flatMap(param -> paymentService.payUrl(param))
                .flatMap(url -> ServerResponse
                        .permanentRedirect(url)
                        .build());
    }
}
