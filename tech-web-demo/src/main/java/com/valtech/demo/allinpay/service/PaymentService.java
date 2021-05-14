package com.valtech.demo.allinpay.service;

import com.google.common.base.Charsets;
import com.valtech.demo.allinpay.entity.OrderQueryReq;
import com.valtech.demo.allinpay.entity.OrderQueryResp;
import com.valtech.demo.allinpay.entity.PayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

@Slf4j
@Component
public class PaymentService {

    private final SignTool signTool;

    private final WebClient webClient;


    public PaymentService(SignTool signTool) {
        this.signTool = signTool;

        DefaultUriBuilderFactory factory=new DefaultUriBuilderFactory("https://test.allinpaygd.com");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        this.webClient=WebClient.builder()
                        .uriBuilderFactory(factory)
                        .build();
    }

    public void query(OrderQueryReq req){

        webClient.post()
                .uri("/apiweb/gateway/query",builder -> {
                    signTool.generPostParams(req,builder);
                    return builder.build();
                })
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe((entry)->{

                    log.info(" body :",entry);

                    OrderQueryResp resp=signTool.verifyResponse(entry,OrderQueryResp.class);
                    log.info("resp content : {}",resp);
                });

    }

    public void pay(PayRequest req){

        webClient.post()
                .uri("/apiweb/gateway/pay",builder->{
                    signTool.generPostParams(req,builder);
                    return builder.build();
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
