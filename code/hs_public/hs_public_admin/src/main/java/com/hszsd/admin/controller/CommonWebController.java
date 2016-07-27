package com.hszsd.admin.controller;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 信息服务提供平台<br/>
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
     * @param request 请求对象
     * @param response 相应对象
     * @return 返回视图界面
     */
    @RequestMapping(value = "index")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("message","你好,服务正常.");
        return  new ModelAndView("index","map",map);
    }
    
    @ResponseBody
    @RequestMapping(value = "hello")
    public Map hello(HttpServletRequest request, HttpServletResponse response){
    	  String accessToken = request.getParameter("access_token");
    	  String usernmae = request.getRemoteUser();
    	  AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();

    	  Map attributes = principal.getAttributes();
         return attributes;
    }
}
