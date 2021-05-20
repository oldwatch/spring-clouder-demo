package com.valtech.demo.allinpay.vo;

import com.google.common.base.Charsets;
import com.valtech.demo.allinpay.entity.PayRequest;
import com.valtech.demo.allinpay.entity.SignType;
import lombok.Data;

@Data
public class SubmitOrderInput {

    private String orderID;

    private String goodsID;

    private String goodsInf;

    private Integer trxAmt;

    private Integer validTime;

    public PayRequest generPayRequest() {

        PayRequest req = new PayRequest();

        req.setCharset(Charsets.UTF_8);
        req.setLimitPay(PayRequest.LimitPayType.no_credit);
        req.setPayType(PayRequest.PayType.B2B);
        req.setSignType(SignType.RSA);

        req.setGoodsID(goodsID);
        req.setGoodsInf(goodsInf);
        req.setTrxAmt(trxAmt);
        req.setOrderID(orderID);
        req.setValidTime(validTime);

        return req;
    }

}
