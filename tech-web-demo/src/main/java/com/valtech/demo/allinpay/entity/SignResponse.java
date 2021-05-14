package com.valtech.demo.allinpay.entity;

import lombok.Data;

@Data
public abstract class SignResponse {

    private String retcode;

    private String retmsg;

    private String cusid;

    private String appid;


    private String randomstr;

    private String errmsg;

    private String sign;

}
