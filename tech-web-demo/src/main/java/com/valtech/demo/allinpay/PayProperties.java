package com.valtech.demo.allinpay;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("com.valtech.demo.allinpay")
@Component
@Data
public class PayProperties {

    private AllInPayCert cert;

    private String retUrl;

    @Data
    public static class AllInPayCert {
        private String clientPrivateKey;

        private String platformPublicKey;


        private String cusID;

        private String appID;

    }

}
