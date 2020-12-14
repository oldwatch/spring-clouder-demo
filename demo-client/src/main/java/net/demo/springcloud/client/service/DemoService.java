package net.demo.springcloud.client.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class DemoService {

    @Value("${demo.client.service_host}")
    private String serviceHost;


    private final RestTemplate  template;

    public DemoService(RestTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedRateString = "PT1M")
    public void task(){

        Map<String,String> values=template.getForObject("http://"+serviceHost+":8080/mock/hello/world",Map.class);

        log.info(" values : {}",values);

    }
}
