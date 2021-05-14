package com.valtech.demo.allinpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AllInPayApplication {

    public static void main(String[] args) {
        SpringApplication application=new SpringApplicationBuilder(AllInPayApplication.class)
                .web(WebApplicationType.REACTIVE)
                .build();

        application.run(args);

    }
}
