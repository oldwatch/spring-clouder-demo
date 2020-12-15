package net.demo.springcloud.react.service;


import feign.Headers;
import feign.Param;
import feign.RequestLine;
import net.demo.springcloud.react.entity.EchoEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

@ReactiveFeignClient(name="demo-service")
public interface EchoClient {

    @GetMapping(path="/mock/hello/{param}",consumes = MediaType.APPLICATION_JSON_VALUE)
    Flux<EchoEntity> getEcho(@PathVariable("param") String hello);
}
