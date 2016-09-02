package com.hszsd.rabbitmq.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 消息队列消费端提供平台<br/>
 * <b>公共页面跳转</b>
 * @author yangwenjian 2016-7-15
 * @version V1.0.0
 */
@Controller
@RequestMapping(value="/commonWeb/")
public class CommonWebController {

    private static final Logger logger = LoggerFactory.getLogger(CommonWebController.class);

    /**
     * 公共页面<br/>
     * <b>首页跳转</b>
     * @return 返回视图界面
     */
    @RequestMapping(value = "index")
    public ModelAndView index(){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("message","你好，消息队列监听端,服务正常.");
        return  new ModelAndView("index","map",map);
    }

}
