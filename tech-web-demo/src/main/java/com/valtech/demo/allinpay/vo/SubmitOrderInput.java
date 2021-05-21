package com.valtech.demo.allinpay.vo;

import com.google.common.base.Charsets;
import com.valtech.demo.allinpay.entity.PayRequest;
import com.valtech.demo.allinpay.entity.SignType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;

@Data
public class SubmitOrderInput {

    private String orderID;

    private String goodsID;

    private String goodsInf;

    private Integer trxAmt;

    private Integer validTime;

    public SubmitOrderInput() {

    }

    public SubmitOrderInput(MultiValueMap<String, String> params) {

        orderID = params.getFirst("orderID");
        goodsID = params.getFirst("goodsID");
        goodsInf = params.getFirst("goodsInf");

        trxAmt = Integer.parseInt(params.getFirst("trxAmt"));
        String time = params.getFirst("validTime");
        if (StringUtils.isNoneBlank(time)) {
            validTime = Integer.parseInt(time);
        }
    }

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
