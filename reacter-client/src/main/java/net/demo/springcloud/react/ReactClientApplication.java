package net.demo.springcloud.react;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@EnableEurekaClient
@EnableReactiveFeignClients
@EnableScheduling
@SpringBootApplication
public class ReactClientApplication {

    public static void main(String[] args) {
        SpringApplication application=new SpringApplicationBuilder(ReactClientApplication.class).web(WebApplicationType.REACTIVE).build();

        application.run(args);



    }

}
