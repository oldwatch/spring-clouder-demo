package com.valtech.demo.allinpay.entity;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderQueryResp extends  SignResponse{

    private String orderid;

    private String trxreserved;

    private String trxid;

    private String trxcode;

    private String trxamt;

    private String trxstatus;

    private String fintime;

}
