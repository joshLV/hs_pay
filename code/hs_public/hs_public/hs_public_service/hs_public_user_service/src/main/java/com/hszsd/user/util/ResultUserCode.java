package com.hszsd.user.util;

import com.hszsd.common.util.ResultCode;

public class ResultUserCode implements ResultCode {
    /**
     * 是否锁定(0开通 )
     */
    public static final int ISLOCK_YES=0;

    /**
     * 是否锁定(1锁定)
     */
    public static final int ISLOCK_NO=1;

    /**
     * 状态(1开通)
     */
    public static final int STATUS_YES=1;

    /**
     * 状态(0关闭)
     */
    public static final int STATUS_NO=0;
}
