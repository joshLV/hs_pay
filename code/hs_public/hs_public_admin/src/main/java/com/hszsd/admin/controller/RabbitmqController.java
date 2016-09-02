package com.hszsd.admin.controller;

import com.hszsd.common.util.DESPlus;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.rabbitmq.dto.MessageEnumDTO;
import com.hszsd.rabbitmq.dto.RabbitMessageDTO;
import com.hszsd.rabbitmq.service.RabbitmqService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户发送信息控制类
 * @version yangwenjian
 * @since 2016-07-22
 * @version V1.0.0
 */
@Controller
@RequestMapping(value = "/public/message/")
public class RabbitmqController {

    private static final Logger logger = LoggerFactory.getLogger(RabbitmqController.class);

    @Autowired
    private RabbitmqService rabbitmqServiceImpl;
    /**
     * 发送消息方法
     * @param request 请求对象
     * @param response 响应对象
     * @param parameter 发送消息加密实体
     * @return 2000 参数缺失<br/>
     */
    @ResponseBody
    @RequestMapping(value = "sendMessage",method = RequestMethod.POST)
    public String sendMessage(HttpServletRequest request, HttpServletResponse response, @Param(value = "parameter") String parameter){
        logger.info("sendMessage rabbitMessageDTO={} ",parameter);
        Result result = new Result();
        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("isExistsPhone DESPlus is error,msg={}",e.getMessage());
            result.setResCode(com.hszsd.user.util.ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证入参是否为空
        if(StringUtils.isEmpty(parameter)){
            logger.info("sendMessage parameter is null");
            result.setResCode(ResultCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密参数
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("sendMessage parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(com.hszsd.user.util.ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        JSONObject jsonObject=JSONObject.fromObject(parameter);
        RabbitMessageDTO rabbitMessageDTO=new RabbitMessageDTO();
        try {
            //验证发送内容
            if(jsonObject.get("content")==null){
                logger.info("sendMessage content is null");
                result.setResMsg("content is null");
                result.setResCode(ResultCode.RES_NONULL);
                return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
            }

            //验证用户ID内容
            if(jsonObject.get("userIds")==null){
                logger.info("sendMessage userIds is null");
                result.setResMsg("userIds is null");
                result.setResCode(ResultCode.RES_NONULL);
                return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
            }

            //发送类型
            if(jsonObject.get("messageType")==null){
                logger.info("sendMessage messageType is null");
                result.setResMsg("messageType is null");
                result.setResCode(ResultCode.RES_NONULL);
                return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
            }
            //发送标题
            if(jsonObject.get("title")==null){
                logger.info("sendMessage title is null");
                result.setResMsg("title is null");
                result.setResCode(ResultCode.RES_NONULL);
                return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
            }
            if(StringUtils.isNotEmpty(String.valueOf(jsonObject.get("content")))){
                rabbitMessageDTO.setContent(String.valueOf(jsonObject.get("content")));
            }
            if(StringUtils.isNotEmpty(String.valueOf(jsonObject.get("title")))){
                rabbitMessageDTO.setTitle(String.valueOf(jsonObject.get("title")));
            }
            //此处因json转枚举异常故以下方法实现
            JSONArray jsonArray  = JSONArray.fromObject(String.valueOf(jsonObject.get("messageType")));
            List<MessageEnumDTO> messageEnumDTO=new ArrayList<MessageEnumDTO>();
            //此处因json转枚举异常故以下方法实现
            for(int i=0;i<jsonArray.size();i++){
                if(StringUtils.isNotEmpty(jsonArray.getString(i))) {
                    messageEnumDTO.add(MessageEnumDTO.valueOf(jsonArray.getString(i)));
                }
            }
            rabbitMessageDTO.setMessageType(messageEnumDTO);
            JSONArray jsonArrayUserId  = JSONArray.fromObject(String.valueOf(jsonObject.get("userIds")));
            List<String> userIdList=new ArrayList<String>();
            for(int i=0;i<jsonArrayUserId.size();i++){
                if(StringUtils.isNotEmpty(jsonArrayUserId.getString(i))){
                    userIdList.add(jsonArrayUserId.getString(i));
                }
            }
            if(userIdList.size()==0){
                logger.info("sendMessage userIds is null");
                result.setResMsg("userIds is null");
                result.setResCode(ResultCode.RES_NONULL);
                return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
            }
            //用户ID放入实体中
            rabbitMessageDTO.setUserIds(userIdList);
            if(StringUtils.isNotEmpty(String.valueOf(jsonObject.get("title")))){
                rabbitMessageDTO.setTitle(String.valueOf(jsonObject.get("title")));
            }
        }catch (Exception e){
            logger.error("sendMessage json is error,msg={}",e.getMessage());
            result.setResCode(ResultCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        result = rabbitmqServiceImpl.sendMessage(rabbitMessageDTO);
        logger.info("sendMessage is success");
        return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));

    }
}