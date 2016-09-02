package com.hszsd.webpay.service;

import java.util.Map;

/**
 * 中天缴费记录业务层接口
 * Created by suocy on 2016/8/25.
 */
public interface ZhongTianFeeService {

    /**
     * 根据ERP通知缴费结果，调功能服务器接口
     * @param params
     * @param flag ture：成功，false：失败
     * @return
     */
    boolean ztwyFeeNotice(Map<String,String> params,boolean flag);


}
