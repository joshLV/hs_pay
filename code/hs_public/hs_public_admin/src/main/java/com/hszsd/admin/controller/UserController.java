package com.hszsd.admin.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hszsd.common.util.DESPlus;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.dto.HttpGetUserInfoDTO;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hszsd.common.util.RegularValidate;
import com.hszsd.common.util.Result;
import com.hszsd.user.service.UserService;
import com.hszsd.user.util.ResultUserCode;

import java.util.List;


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


    private static  String strDefaultKey;

    @Value("#{settings['DESP_KEY']}")
    public  void setStrDefaultKey(String strDefaultKey) {
        UserController.strDefaultKey = strDefaultKey;
    }

    /**
     *  * 获取用户信息<br/>
     * 用户名是否存在<br/>
     * @param request
     * @param response
     * @param parameter
     *            用户名
     * @return result  为true表示存在<br/>
     *         为false表示不存在
     */
    @ResponseBody
    @RequestMapping(value = "isExistsUserName",method =RequestMethod.POST )
    public String isExistsUserName(HttpServletRequest request, HttpServletResponse response,@Param(value = "parameter") String parameter){
        logger.info("isExistsUserName parameter={}",parameter);
        Result result=new Result();
        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("isExistsUserName DESPlus is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证用户名是否为空
        if(StringUtils.isEmpty(parameter)){
            logger.info("isExistsUserName parameter is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密用户名
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("isExistsUserName parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        result=userServiceImpl.isExistsUserName(parameter);
        logger.info("isExistsUserName  is success");
        return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
    }


    /**
     * 获取用户信息<br/>
     * 验证系统中手机号是否存在<br/>
     * @param request
     * @param response
     * @param parameter
     *            手机号
     * @return result 为true表示存在<br/>
     *         为false表示不存在
     */
    @ResponseBody
    @RequestMapping(value = "isExistsPhone",method = RequestMethod.POST)
    public String isExistsPhone(HttpServletRequest request,HttpServletResponse response,@Param(value = "parameter") String parameter){
        logger.info("isExistsPhone parameter={}",parameter);
        Result result=new Result();
        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("isExistsPhone DESPlus is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证手机号不能为空
        if(StringUtils.isEmpty(parameter)){
            logger.error("isExistsPhone parameter is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密手机号
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("isExistsPhone parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        result=userServiceImpl.isExistsPhone(parameter);
        logger.info("isExistsPhone  is success");
        return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
    }

    /**
     *   * 获取用户信息<br/>
     * 验证系统中邮箱是否存在<br/>
     * @param request
     * @param response
     * @param parameter
     *            邮箱
     * @return result  为true表示存在<br/>
     *         为false表示不存在
     */
    @ResponseBody
    @RequestMapping(value = "isExistsEmail",method = RequestMethod.POST)
    public String isExistsEmail(HttpServletRequest request,HttpServletResponse response,@Param(value = "parameter") String parameter){
        logger.info("isExistsEmail parameter={}",parameter);
        Result result=new Result();

        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("isExistsEmail DESPlus is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证邮箱不能为空
        if(StringUtils.isEmpty(parameter)){
            logger.error("isExistsEmail parameter is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密邮箱号
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("isExistsEmail parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        result=userServiceImpl.isExistsEmail(parameter);
        logger.info("isExistsEmail  is success");
        return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
    }

    /**
     * 获取用户信息<br/>
     * 使用用户ID获取用户信息<br/>
     * @param request
     * @param response
     * @param parameter 用户ID
     * @return  0000 执行成功<br/>
     *         2000 参数为空<br/>
     *         1000 系统错误<br/>
     *         5005 账号不存在
     */
    @ResponseBody
    @RequestMapping(value = "getUserInfo",method =RequestMethod.POST)
    public String  getUserInfo(HttpServletRequest request,HttpServletResponse response,@Param(value = "parameter") String parameter){
        logger.info("getUserInfo parameter={}",parameter);
        Result result=new Result();
        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("getUserInfo DESPlus is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证传入参数是否为空
        if(StringUtils.isEmpty(parameter)){
            logger.error("getUserInfo parameter  is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密用户ID
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("getUserInfo parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        result=userServiceImpl.getUserInfo(parameter);
        //因为http请求去除身份证号返回故去除
        if(result.getResCode().equals(ResultUserCode.RES_OK)){
            GetUserInfoDTO getUserInfoDTO=(GetUserInfoDTO)result.getResult();
            HttpGetUserInfoDTO httpGetUserInfoDTO=new HttpGetUserInfoDTO();
            BeanUtils.copyProperties(getUserInfoDTO,httpGetUserInfoDTO);
            result.setResult(httpGetUserInfoDTO);
        }
        logger.info("getUserInfo is success");
        return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
    }

    /**
     * 获取用户信息<br/>
     * 使用用户ID获取用户信息,返回身份证号<br/>
     * @param request
     * @param response
     * @param parameter 用户ID
     * @return  0000 执行成功<br/>
     *         2000 参数为空<br/>
     *         1000 系统错误<br/>
     *         5005 账号不存在
     */
    @ResponseBody
    @RequestMapping(value = "getUserInfoCardId",method =RequestMethod.POST)
    public String  getUserInfoCardId(HttpServletRequest request,HttpServletResponse response,@Param(value = "parameter") String parameter){
        logger.info("getUserInfoCardId parameter={}",parameter);
        Result result=new Result();
        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("getUserInfoCardId DESPlus is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证传入参数是否为空
        if(StringUtils.isEmpty(parameter)){
            logger.error("getUserInfoCardId parameter  is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密用户ID
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("getUserInfoCardId parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        result=userServiceImpl.getUserInfo(parameter);
        logger.info("getUserInfoCardId is success");
        return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
    }

    /**
     * 获取用户信息<br/>
     * 使用用户传递过来的用户名或者是手机号<br/>
     * @param request
     * @param response
     * @param parameter 传递参数
     * @return 0000 执行成功<br/>
     *         2000 参数为空<br/>
     *         1000 系统错误<br/>
     *         5005 账号不存在
     */
    @ResponseBody
    @RequestMapping(value = "getUser",method =RequestMethod.POST)
    public String  getUser(HttpServletRequest request,HttpServletResponse response,@Param(value = "parameter") String parameter){
        logger.info("getUser parameter={}",parameter);
        Result result=new Result();
        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("getUser DESPlus is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证传入参数是否为空
        if(StringUtils.isEmpty(parameter)){
            logger.error("getUser param  is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密传进来的参数
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("getUser parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        result=userServiceImpl.getUser(parameter);
        logger.info("getUser is success");
        return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
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
        Result result=userServiceImpl.isExistsUserDESPPayPassword(userId,payPassword);
        logger.info("isExistsUserPayPassword is success");
        return result;

    }


    /**
     * 修改用户来源
     * @param parameter 加密后的用户ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateUserBlockSource",method = RequestMethod.POST)
    public String updateUserBlockSource(String parameter){
        logger.info("updateUserBlockSource parameter={}",parameter);
        Result result=new Result();
        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("updateUserBlockSource DESPlus is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证传入参数是否为空
        if(StringUtils.isEmpty(parameter)){
            logger.error("updateUserBlockSource parameter  is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密用户ID
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("updateUserBlockSource parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }

        JSONObject jsonObject=JSONObject.fromObject(parameter);
        if(jsonObject.get("userId")==null||jsonObject.get("blockSource")==null){
            logger.error("updateUserBlockSource parameter is userId or blockSource is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try {
            result=userServiceImpl.updateUserBlockSource(jsonObject.get("userId").toString(),jsonObject.get("blockSource").toString());
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        } catch (Exception e){
            logger.error("updateUserBlockSource is error msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }


    }


    /**
     * 手机号身份证用户信息反查
     * @param parameter
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "counterUser",method = RequestMethod.POST)
    public String counterUser(String parameter){
        logger.info("counterUser parameter={}",parameter);
        Result result=new Result();
        DESPlus desPlus= null;
        try {
            desPlus = new DESPlus();
        } catch (Exception e) {
            logger.error("counterUser DESPlus is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.RES_NO);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        //验证传入参数是否为空
        if(StringUtils.isEmpty(parameter)){
            logger.error("counterUser parameter  is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        try{
            //解密用户ID
            parameter=desPlus.decrypt(parameter);
        }catch (Exception e){
            logger.error("counterUser parameter deciphering is error,msg={}",e.getMessage());
            result.setResCode(ResultUserCode.DESP_ERROR);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        JSONObject jsonObject=JSONObject.fromObject(parameter);
        if(jsonObject.get("phones")==null||jsonObject.get("cards")==null){
            logger.error("counterUser parameter is phones or cards  is null");
            result.setResCode(ResultUserCode.RES_NONULL);
            return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
        }
        List<String> phones=(List<String>)jsonObject.get("phones");
        List<String> cards=(List<String>)jsonObject.get("cards");
        result=userServiceImpl.counterUser(phones,cards);
        return desPlus.encrypt(String.valueOf(JSONObject.fromObject(result)));
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
