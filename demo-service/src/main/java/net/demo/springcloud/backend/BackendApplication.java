package net.demo.springcloud.backend;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BackendApplication.class).run(args);
    }
}
