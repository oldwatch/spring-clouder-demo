package net.demo.springcloud.react.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactivefeign.java11.Java11ReactiveFeign;

@Slf4j
@Component
public class EchoService {

    private final EchoClient client;

    public EchoService(EchoClient client) {
        this.client = client;
    }


    @Scheduled(fixedRateString = "PT1M")
    public void task(){

        client.getEcho("world")
                .subscribe(entry->{
            log.info(" values : {}",entry);
        });

    }

    @Scheduled(fixedRateString = "PT1M")
    public void task2(){

        EchoClientInterface webClient=
                Java11ReactiveFeign.<EchoClientInterface>builder()
                .target(EchoClientInterface.class,"http://localhost:8080");


        webClient.getEcho("world")
                .subscribe(entry->{
                    log.info(" values : {}",entry);
                });

    }


}
