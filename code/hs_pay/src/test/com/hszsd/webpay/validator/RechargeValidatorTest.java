package com.hszsd.webpay.validator;

import com.hszsd.webpay.web.dto.TradeRecordDTO;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;

/**
 * Created by gzhengDu on 2016/7/19.
 */
public class RechargeValidatorTest {

    @Test
    public void validate() throws Exception {
        RechargeValidator validator = new RechargeValidator();
        TradeRecordDTO tradeRecordDTO = new TradeRecordDTO();
        tradeRecordDTO.setTransId("1111");
        DataBinder result = new DataBinder(tradeRecordDTO);
        BindingResult bindingResult = result.getBindingResult();
        validator.validate(tradeRecordDTO, bindingResult);
        for(FieldError error:bindingResult.getFieldErrors()){
            System.out.println(error.getField()+error.getArguments()[0]);
        }
        System.out.println(bindingResult.hasErrors());
    }
}