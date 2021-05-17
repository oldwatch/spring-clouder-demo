package test;

import com.google.common.base.Charsets;
import com.valtech.demo.allinpay.AllInPayApplication;
import com.valtech.demo.allinpay.entity.OrderQueryReq;
import com.valtech.demo.allinpay.entity.PayRequest;
import com.valtech.demo.allinpay.entity.SignType;
import com.valtech.demo.allinpay.service.PaymentService;
import com.valtech.demo.allinpay.service.SignTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@Slf4j
//@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllInPayApplication.class)
public class TestPayCall {

    @Autowired
    private PaymentService service;

    @Autowired
    private SignTool signTool;

    DefaultUriBuilderFactory factory=new DefaultUriBuilderFactory();

    @Before
    public void init(){

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
    }


    @Test
    public void testCallPay() throws InterruptedException {
        PayRequest req = getPayRequest();

        log.info(" orderID : {}",req.getOrderID());

        service.pay(req);

        Thread.sleep(1000*10);
    }

    @Test
    public void testCallQuery()throws  InterruptedException{

        OrderQueryReq req=getQueryRequest();

        service.query(req);

        Thread.sleep(1000*10);

    }

    @Test
    public void testQueryGener(){
        OrderQueryReq req = getQueryRequest();

        UriBuilder builder=factory.builder();

        signTool.fillPostParams(req, builder);

        log.info("full Post params {}",builder.build());
    }




    private OrderQueryReq getQueryRequest() {
        OrderQueryReq req=new OrderQueryReq();

        req.setSignType(SignType.RSA);

        req.setCusID("990581007426001");
        req.setAppID("00000051");
        req.setRandomStr("bv2nhWPt8i4aLm3ld6O2YvrWTmPBMQrM");

        req.setOrderID("1620978514903");
        return req;
    }

    private DemoReq getDemoReq(){
        /*
        如appid=00000051
cusid=990581007426001
randomstr=82712208
signtype=RSA
trxid=112094120001088317
version=11
排序后的字符串：
string=”appid=00000051&cusid=990581007426001&randomstr=82712208&signtype=RSA&trxid=112094120001088317&version=11”;


         */
        DemoReq request=new DemoReq();
        request.setAppID("00000051");
        request.setCusID("990581007426001");
        request.setRandomStr("82712208");
        request.setSignType(SignType.RSA);
        request.setTrxID("112094120001088317");
        request.setVersion("11");

        return request;
    }

    private PayRequest getPayRequest() {
        PayRequest req=new PayRequest();

        req.setCharset(Charsets.UTF_8);
        req.setLimitPay(PayRequest.LimitPayType.no_credit);
        req.setPayType(PayRequest.PayType.B2B);
        req.setSignType(SignType.RSA);
        req.setRandomStr("82712208");

        req.setCusID("990581007426001");
        req.setAppID("00000051");

        req.setRetUrl("test.bing.com");
        req.setGoodsID("goods ID");
        req.setGoodsInf("goods info");
        req.setTrxAmt(123456);
        req.setOrderID("1620978514903");
        return req;
    }
}
