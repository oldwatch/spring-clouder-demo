package com.valtech.demo.allinpay.service;

import com.google.common.base.Charsets;
import com.valtech.demo.allinpay.PayProperties;
import com.valtech.demo.allinpay.entity.OrderQueryReq;
import com.valtech.demo.allinpay.entity.OrderQueryResp;
import com.valtech.demo.allinpay.entity.PayRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class PaymentService {

    private final SignTool signTool;

    private final WebClient webClient;

    private final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("https://test.allinpaygd.com");


    private final PayProperties properties;

    public PaymentService(SignTool signTool, PayProperties payProperties) {
        this.signTool = signTool;

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        this.properties = payProperties;
        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();
    }

    public void query(OrderQueryReq req) {

        webClient.post()
                .uri("/apiweb/gateway/query",builder -> {
                    signTool.fillPostParams(req, builder);
                    return builder.build(true);
                })
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe((entry) -> {

                    log.info(" body :", entry);

                    OrderQueryResp resp = signTool.verifyResponse(entry, OrderQueryResp.class);
                    log.info("resp content : {}", resp);
                });

    }

    public Mono<URI> payUrl(PayRequest req) {


        req.setCusID(properties.getCert().getCusID());
        req.setAppID(properties.getCert().getAppID());
        req.setRetUrl(properties.getRetUrl());
        if (req.getRandomStr() == null) {
            req.setRandomStr(RandomStringUtils.randomAlphanumeric(32));
        }

        return signTool
                .reactivePostParams(req, factory.builder())
                .map(builder -> builder.build()).log();
    }

    public void pay(PayRequest req) {

        webClient.post()
                .uri("/apiweb/gateway/pay", builder -> {
                    signTool.fillPostParams(req, builder);
                    return builder.build(true);
                })
                .acceptCharset(Charsets.UTF_8)
                .exchange()
                .subscribe((response)->{
                   log.info("status code {}",response.rawStatusCode());
                   response.headers().asHttpHeaders().forEach((key,list)->{
                       log.info(" header name:{}, value {]",key,list);
                   });

                   response.bodyToMono(String.class).subscribe(str->{
                      log.info(" body {}",str);
                   });
                });

    }




}
