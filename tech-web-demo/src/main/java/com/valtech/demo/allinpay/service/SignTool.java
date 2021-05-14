package com.valtech.demo.allinpay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.valtech.demo.allinpay.entity.SignRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.comparator.Comparators;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SignTool {

    private final static String PLATFORM_RSA_PK=
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDYXfu4b7xgDSmEGQpQ8Sn3RzFgl5CE4gL4TbYrND4FtCYOrvbgLijkdFgIrVVWi2hUW" +
                    "4K0PwBsmlYhXcbR+JSmqv9zviVXZiym0lK3glJGVCN86r9EPvNTusZZPm40TOEKMVENSYaUjCxZ7JzeZDfQ4WCeQQr2xirqn6" +
                    "LdJjpZ5wIDAQAB";

    private final static String CLIENT_RSA_PK=
            "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJgHMGYsspghvP+yCbjLG43CkZuQ3YJyDcmEKxvmgbl" +
                    "ITfmiTPx2b9Y2iwDT9gnLGExTDm1BL2A8VzMobjaHfiCmTbDctu680MLmpDDkVXmJOqdlXh0tcLjhN4+iDA2KkRqiHxsDpiaKT6MMBuec" +
                    "XQbJtPlVc1XjVhoUlzUgPCrvAgMBAAECgYAV9saYTGbfsdLOF5kYo0dve1JxaO7dFMCcgkV+z2ujKtNmeHtU54DlhZXJiytQY5Dhc10cj" +
                    "b6xfFDrftuFcfKCaLiy6h5ETR8jyv5He6KH/+X6qkcGTkJBYG1XvyyFO3PxoszQAs0mrLCqq0UItlCDn0G72MR9/NuvdYabGHSzEQJBAM" +
                    "XB1/DUvBTHHH4LiKDiaREruBb3QtP72JQS1ATVXA2v6xJzGPMWMBGQDvRfPvuCPVmbHENX+lRxMLp39OvIn6kCQQDEzYpPcuHW/7h3TYH" +
                    "Yc+T0O6z1VKQT2Mxv92Lj35g1XqV4Oi9xrTj2DtMeV1lMx6n/3icobkCQtuvTI+AcqfTXAkB6bCz9NwUUK8sUsJktV9xJN/JnrTxetOr3" +
                    "h8xfDaJGCuCQdFY+rj6lsLPBTnFUC+Vk4mQVwJIE0mmjFf22NWW5AkAmsVaRGkAmui41Xoq52MdZ8WWm8lY0BLrlBJlvveU6EPqtcZskW" +
                    "W9KiU2euIO5IcRdpvrB6zNMgHpLD9GfMRcPAkBUWOV/dH13v8V2Y/Fzuag/y5k3/oXi/WQnIxdYbltad2xjmofJ7DbB7MJqiZZD8jlr8P" +
                    "CZPwRNzc5ntDStc959";

    private final Signature clientSignature;

    private final Signature platformSignature;

    private final ObjectMapper mapper=new ObjectMapper();

    public SignTool() throws NoSuchAlgorithmException {

        clientSignature=Signature
                .getInstance("SHA1WithRSA");

        platformSignature=Signature
                .getInstance("SHA1WithRSA");
    }

    @PostConstruct
    public void init(){

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(CLIENT_RSA_PK)));
            clientSignature.initSign(priKey);

            PublicKey pubKey= keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(PLATFORM_RSA_PK)));
            platformSignature.initVerify(pubKey);

        }catch (GeneralSecurityException e) {
            log.error("init RSA signature tool fail ");
            throw new IllegalArgumentException(e);
        }
    }

    private final Map<Class, List<Tuple2<String,Method>>> methodListCache= Maps.newConcurrentMap();


    private List<Tuple2<String,Method>> generMethodList(Class<? extends  SignRequest> cls){

        return Flux.fromArray(cls.getMethods())
                .filter((m)-> m.getName().startsWith("get")&&(!m.getName().equals("getClass")))
                .map((m)-> Tuples.of(m.getName().substring(3).toLowerCase(Locale.ROOT),m))
                .sort((t1,t2)-> Comparators.comparable().compare(t1.getT1(),t2.getT1()))
                .toStream().collect(Collectors.toList());

    }


    private  String rsaSign(String content)  {


        try {
            clientSignature.update(content.getBytes(Charsets.UTF_8));

            byte[] signed = clientSignature.sign();

            return new String(Base64.getEncoder().encode(signed));
        } catch (SignatureException e) {
            log.error(" signature operate fail:content {}",content);
            throw new IllegalArgumentException(e);
        }

    }

    private boolean rsaVerify(String content,String sign){
        try {
            platformSignature.update(content.getBytes(Charsets.UTF_8));

            return  platformSignature.verify(sign.getBytes());

        } catch (SignatureException e) {
            log.error(" signature operate fail:content {}",content);
            throw new IllegalArgumentException(e);
        }

    }

    public <T> T verifyResponse(String jsonStr,Class<T> cls ){

        JsonNode node= null;
        try {
            node = mapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            log.error(" json parse fail {}",jsonStr);
            throw new IllegalArgumentException(e);
        }

        SortedMap<String,String> orderMap=Maps.newTreeMap();

        node.fields().forEachRemaining(kv->{

            String field=kv.getKey();

            if(field.equals("sign")){
                return;
            }

            JsonNode n=kv.getValue();

            if(n.isNull() || !n.isValueNode()) {
                return;
            }

            orderMap.put(field,n.textValue());

        });

        StringBuilder buf=orderMap.entrySet().stream()
                .reduce(new StringBuilder(),
                        (sb,e)->sb.append("&").append(e.getKey()).append("=").append(e.getValue()),
                        (sb1,sb2)->sb1.append(sb2));

        buf.deleteCharAt(0);
        String sign=node.get("sign").asText();

        if (!rsaVerify(buf.toString(),sign)){
            return null;
        }

        try {
            return mapper.readValue(jsonStr,cls);
        } catch (JsonProcessingException e) {
            log.error("json parse to {} fail",cls.getName());
            throw new IllegalArgumentException(e);
        }

    }

    public void generPostParams(SignRequest req,UriBuilder builder){

        if(req.getRandomStr()==null) {
            req.setRandomStr(RandomStringUtils.randomAlphanumeric(32));
        }

        StringBuilder buffer=(StringBuilder)
                methodListCache.computeIfAbsent(req.getClass(), this::generMethodList)
                .stream()
                .reduce(new StringBuilder()
                    , (BiFunction<StringBuilder, Tuple2<String, Method>, StringBuilder>) (sb, tuple) -> {
                    try {
                        Object val = tuple.getT2().invoke(req);
                        if (val != null) {
                            builder.queryParam(tuple.getT1(),URLEncoder.encode(String.valueOf(val),Charsets.UTF_8));
                            sb.append("&").append(tuple.getT1()).append("=").append(val);
                        }
                    } catch (Exception e) {
                        log.warn(" read field {} error ", tuple.getT1());
                    }
                    return sb;
                    }
                    ,(BinaryOperator<StringBuilder>) (sb1, sb2)->sb1.append(sb2));

        buffer.deleteCharAt(0);

        String sign=rsaSign(buffer.toString());

        builder.queryParam("sign",URLEncoder.encode(sign,Charsets.UTF_8));
//        return buffer.toString()+"&sign="+sign;

    }

    @Data
    @AllArgsConstructor
    public static class UrlInfo{

        private String queryStr;

        private String sign;


    }




}
