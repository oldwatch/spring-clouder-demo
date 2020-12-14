package net.demo.springcloud.client.service;

import net.demo.springcloud.client.entity.EchoEntry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("demo-service")
public interface EchoClient {

    @GetMapping(value = "/mock/hello/{param}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public EchoEntry doEcho(@PathVariable("param")  String param);

}
