package com.hszsd.webpay.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 根路径拦截器
 * 用于设置根路径值
 * Created by gzhengDu on 2016/6/30.
 */
public class BasePathInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer basePath = new StringBuffer();
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String path = request.getContextPath();
        basePath.append(scheme).append("://").append(serverName).append(":").append(port).append(path);
        request.setAttribute("basePath", basePath.toString());
        request.setAttribute("ctxPath", path);
        return true;
    }
}
