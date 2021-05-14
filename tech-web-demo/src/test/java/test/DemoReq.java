package test;

import com.valtech.demo.allinpay.entity.SignRequest;
import lombok.Data;

@Data
public class DemoReq extends SignRequest {

    private String version;

    private String trxID;

}
