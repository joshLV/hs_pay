package com.hszsd.admin.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hszsd.admin.util.CasUtil;
import com.hszsd.admin.util.RegularValidate;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.ReturnMsg;
import com.hszsd.user.service.UserService;
import com.hszsd.user.util.ResultUserCode;

/**
 * 信息服务提供平台<br/>
 * <b>获取用户信息<b/>
 * @author yangwenjian
 * @since 2016-7-15
 * @version V1.0.0
 */
@Controller
@RequestMapping(value = "/public/user/")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userServiceImpl;
   
//    @Autowired
//    private CasUtil casUtil;
    /**
     *  * 获取用户信息<br/>
     * 用户名是否存在<br/>
     * @param request
     * @param response
     * @param username
     *            用户名
     * @return  为true表示存在<br/>
     *         为false表示不存在
     */
    @ResponseBody
    @RequestMapping(value = "isExistsUserName")
    public Result isExistsUserName(HttpServletRequest request, HttpServletResponse response,@Param(value = "username") String username){
        logger.info("isExistsUserName username={}",username);
        Result result=new Result();
        //验证用户名是否为空
        if(StringUtils.isEmpty(username)){
            logger.error("isExistsUserName username is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return result;
        }
        ReturnMsg returnMsg=userServiceImpl.isExistsUserName(username);
        logger.info("isExistsUserName  is success");
        result.setResCode(ResultUserCode.RES_OK);
        result.setResult(returnMsg.isSuccess());
        return result;
    }

    /*@RequestMapping(value = "checkName")
    public void checkName(HttpServletRequest request, HttpServletResponse response,String code){
    	 String ismurl = casUtil.getCasUrl()+"?client_id="+casUtil.getClientId()+"&client_secret="+casUtil.getClientSecret()+"&grant_type="+casUtil.getGrantType()+"&redirect_uri="+casUtil.getRedirectUri()+"&code="+code;
         try {
             response.sendRedirect(ismurl);
         }catch (Exception e){
             e.fillInStackTrace();
         }
    }
    */

    /**
     * 获取用户信息<br/>
     * 验证系统中手机号是否存在<br/>
     * @param request
     * @param response
     * @param phone
     *            手机号
     * @return  为true表示存在<br/>
     *         为false表示不存在
     */
    @ResponseBody
    @RequestMapping(value = "isExistsPhone",method = RequestMethod.POST)
    public Result isExistsPhone(HttpServletRequest request,HttpServletResponse response,@Param(value = "phone") String phone){
        logger.info("isExistsPhone phone={}",phone);
        Result result=new Result();
        //验证手机号不能为空
        if(StringUtils.isEmpty(phone)){
            logger.error("isExistsPhone phone is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return result;
        }
        //验证手机号是否合法
        if(!RegularValidate.isValidatePhoneRegular(phone)){
            logger.error("isExistsPhone phone validate is error");
            result.setResCode(ResultUserCode.PARAM_VALIDATE_ERROR);
            return result;
        }
        ReturnMsg returnMsg=userServiceImpl.isExistsPhone(phone);
        logger.info("isExistsPhone  is success");
        result.setResCode(ResultUserCode.RES_OK);
        result.setResult(returnMsg.isSuccess());
        return result;
    }

    /**
     *   * 获取用户信息<br/>
     * 验证系统中邮箱是否存在<br/>
     * @param request
     * @param response
     * @param email
     *            邮箱
     * @return  为true表示存在<br/>
     *         为false表示不存在
     */
    @ResponseBody
    @RequestMapping(value = "isExistsEmail",method = RequestMethod.POST)
    public Result isExistsEmail(HttpServletRequest request,HttpServletResponse response,@Param(value = "email") String email){
        logger.info("isExistsEmail email={}",email);
        Result result=new Result();
        //验证邮箱不能为空
        if(StringUtils.isEmpty(email)){
            logger.error("isExistsEmail email is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return result;
        }
        //验证邮箱是否合法
        if(!RegularValidate.isValidateEmailRegular(email)){
            logger.error("isExistsEmail email validate is error");
            result.setResCode(ResultUserCode.PARAM_VALIDATE_ERROR);
            return result;
        }
        ReturnMsg returnMsg=userServiceImpl.isExistsEmail(email);
        logger.info("isExistsEmail  is success");
        result.setResCode(ResultUserCode.RES_OK);
        result.setResult(returnMsg.isSuccess());
        return result;
    }

    /**
     * 获取用户信息<br/>
     * 使用用户ID获取用户信息<br/>
     * @param request
     * @param response
     * @param userId 用户ID
     * @return  0000 执行成功<br/>
     *         2000 参数为空<br/>
     *         1000 系统错误<br/>
     *         5005 账号不存在
     */
    @ResponseBody
    @RequestMapping(value = "getUserInfo",method =RequestMethod.POST)
    public Result  getUserInfo(HttpServletRequest request,HttpServletResponse response,@Param(value = "userId") String userId){
        logger.info("getUserInfo userId={}",userId);
        Result result=new Result();
        //验证传入参数是否为空
        if(StringUtils.isEmpty(userId)){
            logger.error("getUserInfo userId  is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return result;
        }
        result=userServiceImpl.getUserInfo(userId);
        logger.info("getUserInfo is success");
        return result;
    }

    /**
     * 获取用户信息<br/>
     * 使用用户传递过来的用户名或者是手机号<br/>
     * @param request
     * @param response
     * @param param 传递参数
     * @return 0000 执行成功<br/>
     *         2000 参数为空<br/>
     *         1000 系统错误<br/>
     *         5005 账号不存在
     */
    @ResponseBody
    @RequestMapping(value = "getUser",method =RequestMethod.POST)
    public Result  getUser(HttpServletRequest request,HttpServletResponse response,@Param(value = "param") String param){
        logger.info("getUser param={}",param);
        Result result=new Result();
        //验证传入参数是否为空
        if(StringUtils.isEmpty(param)){
            logger.error("getUser param  is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return result;
        }
        result=userServiceImpl.getUser(param);
        logger.info("getUser is success");
        return result;
    }


    /**
     * 验证用户支付密码是否正确
     * @param request
     * @param response
     * @param userId 用户ID
     * @param payPassword 用户支付密码
     * @return  0000 执行成功<br/>
     *          1000 操作失败<br/>
     *          2000 参数缺失<br/>
     *          5005 账号不存在<br/>
     *          5009 支付密码错误<br/>
     *          5011 解密失败<br/>
     *          5000 账号冻结<br/>
     *          5004 账号注销<br/>
     *          5010 未设置支付密码
     */
    @ResponseBody
    @RequestMapping(value = "isExistsUserPayPassword",method = RequestMethod.POST)
    public Result isExistsUserPayPassword(HttpServletRequest request,HttpServletResponse response,@Param(value = "userId") String userId,
                                          @Param(value = "payPassword") String  payPassword){
        logger.info("isExistsUserPayPassword userId={} or payPassword={}",userId,payPassword);
        Result result=new Result();
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(payPassword)){
            logger.error("isExistsUserPayPassword userId or payPassword is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return result;
        }

        result=userServiceImpl.isExistsUserDESPPayPassword(userId,payPassword);
        logger.info("isExistsUserPayPassword is success");
        return result;

    }

    /**
     * 获取用户信息<br/>
     * 根据用户名密码获取用户信息<br/>
     * @param request
     * @param response
     * @param username 用户名
     * @param password 密码
     * @return
     */
    /*@ResponseBody
    @RequestMapping(value = "getNameUserInfo",method = RequestMethod.POST)
    public Result  getNameUserInfo(HttpServletRequest request, HttpServletResponse response, @Param(value = "username") String  username,@Param(value = "password") String password){
        logger.info("getNameUserInfo username={} and password={}",username,password);
        Result result=new Result();
        //验证传入参数是否为空
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            logger.error("getNameUserInfo username or password is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return result;
        }
        //调用service
        result=userServiceImpl.getNameUserInfo(username,password);
        logger.info("getNameUserInfo is success");
        return result;
    }*/

  /**
     * 获取用户信息<br/>
     * 根据手机号密码获取用户信息<br/>
     * @param request
     * @param response
     * @param phone 手机号
     * @param password 密码
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "getPhoneUserInfo",method =RequestMethod.POST)
    public Result getPhoneUserInfo(HttpServletRequest request,HttpServletResponse response,@Param(value = "phone") String phone,
                                 @Param(value = "password") String password){
        logger.info("getPhoneUserInfo phone={} and password={}",phone,password);
        Result result=new Result();
        //验证传入参数是否为空
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)){
            logger.error("getPhoneUserInfo phone or password is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return result;
        }
        //验证手机号是否合法
        if(RegularValidate.isValidatePhoneRegular(phone)){
            logger.error("getPhoneUserInfo phone validate is error");
            result.setResCode(ResultUserCode.PARAM_VALIDATE_ERROR);
            return result;
        }
        result=userServiceImpl.getPhoneUserInfo(phone,password);
        logger.info("getPhoneUserInfo is success");
        return result;
    }
*/



}
