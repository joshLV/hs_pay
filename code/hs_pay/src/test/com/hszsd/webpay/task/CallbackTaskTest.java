package com.hszsd.webpay.task;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gzhengDu on 2016/7/29.
 */
public class CallbackTaskTest {

    @Test
    public void doCallBack() throws Exception {
        CallbackTask callbackTask = new CallbackTask();
        callbackTask.doCallBack();
    }
}