package com.valtech.demo.allinpay.entity;


import lombok.Data;

@Data
public abstract class SignRequest {


    private String orgID;

    private String cusID;

    private String appID;

    private SignType signType;

    private String randomStr;

    private String orderID;

}
