package com.valtech.demo.allinpay.entity;

import lombok.Data;

import java.nio.charset.Charset;

@Data
public class PayRequest extends SignRequest {


    private Charset charset;

    private String retUrl;

    private String goodsID;

    private String goodsInf;

    private Integer trxAmt;

    private PayType payType;

    private Integer validTime;

    private LimitPayType limitPay;


    public enum PayType{
        B2C,B2B
    }
    public enum LimitPayType{
        no_credit,to_credit;
    }


}
