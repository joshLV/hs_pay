package com.hszsd.webpay.service;

import com.hszsd.webpay.common.ResultInfo;

/**
 * 解绑快捷卡业务层接口
 * Created by gzhengDu on 2016/8/15.
 */
public interface UnbindService {
    /**
     * 易宝查询绑卡列表
     * @param userId 用户标识ID
     * @return
     */
    ResultInfo yeePayQueryBind(String userId);

    /**
     * 易宝解绑卡
     * @param userId 用户标识ID
     * @param bindId 绑卡ID
     * @param cardTop 卡前四位
     * @param cardLast 卡后四位
     * @return
     */
    ResultInfo yeePayUnbind(String userId, String bindId, String cardTop, String cardLast);

}
