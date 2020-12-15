package net.demo.springcloud.react.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import net.demo.springcloud.react.entity.EchoEntity;
import reactor.core.publisher.Flux;

public interface EchoClientInterface {

    @RequestLine("GET /mock/hello/{param}")
    @Headers({"Accept: application/json"})
    Flux<EchoEntity> getEcho(@Param("param") String hello);

}
