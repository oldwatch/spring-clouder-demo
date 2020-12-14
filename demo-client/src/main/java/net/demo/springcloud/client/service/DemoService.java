package net.demo.springcloud.client.service;

import lombok.extern.slf4j.Slf4j;
import net.demo.springcloud.client.entity.EchoEntry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DemoService {


    private final EchoClient echoClient;

    public DemoService(EchoClient echoClient){

        this.echoClient = echoClient;
    }

    @Scheduled(fixedRateString = "PT1M")
    public void task(){

        EchoEntry entry =echoClient.doEcho("world");

        log.info(" values : {}",entry);

    }
}
