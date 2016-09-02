package com.hszsd.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hszsd.entity.Userinfo;
import com.hszsd.entity.example.UserinfoExample;
import com.hszsd.user.dao.UserinfoMapper;
import com.hszsd.user.dto.PhoneMailDTO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hszsd.common.util.DESPlus;
import com.hszsd.common.util.MD5Util;
import com.hszsd.common.util.RegularValidate;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.entity.User;
import com.hszsd.user.dao.TbUserMapper;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.dto.UserDTO;
import com.hszsd.user.po.TbUser;
import com.hszsd.user.po.TbUserExample;
import com.hszsd.user.po.UserModel;
import com.hszsd.user.service.UserService;
import com.hszsd.user.util.ResultUserCode;

/**
 * 用户信息接口实现类<br/>
 * 贵州合石电子商务有限公司
 * 
 * @author yangwenjian
 * @since 2016-07-20
 * @version V1.0.0
 * 
 */
@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);
	@Autowired
	public TbUserMapper tbUserMapper;

	@Autowired
	private UserinfoMapper userinfoMapper;

	@Value("#{settings['URL_HEAD']}")
	private String URL_HEAD;

	/**
	 * 验证系统中用户名是否存在
	 * 
	 * @param username
	 *            用户名
	 * @return result 为true表示存在<br/>
	 *         为false表示不存在
	 */
	@Override
	public Result isExistsUserName(String username) {
		logger.info("isExistsUserName username={}", username);
		Result result = new Result();
		// 进行空值判断
		if (StringUtils.isEmpty(username)) {
			logger.info("isExistsUserName username is null");
			result.setResCode(ResultUserCode.RES_NONULL);
			return result;
		}
		// 封装查询条件
		try {
			result = getUser(username);
		} catch (Exception e) {
			logger.error("isExistsUserName is error msg={}", e.getMessage());
			result.setResCode(ResultUserCode.RES_NO);
			return result;
		}
		logger.info("isExistsUserName  is success");
		if(ResultUserCode.RES_OK.equals(result.getResCode())){
			UserDTO user=(UserDTO)result.getResult();
			if(!username.equals(user.getUsername())){
				// 设置返回状态
				result.setResCode(ResultUserCode.USER_NO_USERNAME);
			}else{
				result.setResult(user.getUserId());
			}
		}
		return result;
	}

	/**
	 * 验证系统中手机号是否存在
	 * 
	 * @param phone
	 *            手机号
	 * @return result 为true表示存在<br/>
	 *         为false表示不存在
	 */
	@Override
	public Result isExistsPhone(String phone) {
		logger.info("isExistsPhone phone={}", phone);
		Result result = new Result();
		// 进行空值判断
		if (StringUtils.isEmpty(phone)) {
			logger.info("isExistsPhone phone is null");
			result.setResCode(ResultUserCode.RES_NONULL);
			return result;
		}
		// 验证手机号是否合法
		if (!RegularValidate.isValidatePhoneRegular(phone)) {
			logger.error("isExistsPhone phone validate is error");
			result.setResCode(ResultUserCode.RES_NOSYS);
			return result;
		}
		// 封装查询条件
		try {
			result = getUser(phone);
		} catch (Exception e) {
			logger.error("isExistsPhone is error msg={}", e.getMessage());
			result.setResCode(ResultUserCode.RES_NO);
			return result;
		}
		logger.info("isExistsPhone  is success");
		// 设置返回状态
		if(ResultCode.RES_OK.equals(result.getResCode())){
			UserDTO user=(UserDTO)result.getResult();
			if(!phone.equals(user.getPhone())){
				// 设置返回状态
				result.setResCode(ResultUserCode.USER_NO_PHONE);
			}else{
				result.setResult(user.getUserId());
			}
		}
		return result;
	}

	/**
	 * 验证系统中邮箱是否存在
	 * 
	 * @param email
	 *            邮箱
	 * @return result 为true表示存在<br/>
	 *         为false表示不存在
	 */
	@Override
	public Result isExistsEmail(String email) {
		logger.info("isExistsEmail email={}", email);
		Result result = new Result();
		// 进行空值判断
		if (StringUtils.isEmpty(email)) {
			logger.info("isExistsEmail email is null");
			result.setResCode(ResultUserCode.RES_NONULL);
			return result;
		}
		// 验证邮箱是否合法
		if (!RegularValidate.isValidateEmailRegular(email)) {
			logger.error("isExistsEmail email validate is error");
			result.setResCode(ResultUserCode.RES_NOSYS);
			return result;
		}
		// 封装查询条件
		TbUser user = new TbUser();
		user.setEmail(email);
		List<TbUser> list = tbUserMapper.selectByExample(getExample(user));
		if(null!=list&&list.size()>0){
			logger.info("isExistsEmail  is success");
			user = list.get(0);
			// 设置返回状态
			result.setResCode(ResultUserCode.RES_OK);
			result.setResult(user.getUserId());
		}else{
			result.setResCode(ResultUserCode.USER_NO_EMAIL);
		}
		return result;
	}

	/**
	 * 获取总条数
	 * 
	 * @param user
	 *            用户对象
	 * @return 符合条件的总条数
	 */
	private int getUserCount(TbUser user) {
		int count = 0;
		TbUserExample example = getExample(user);
		// 查询符合条件的总数
		count = tbUserMapper.countByExample(example);
		return count;
	}

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return 0000 执行成功<br/>
	 *         2000 参数为空<br/>
	 *         1000 系统错误<br/>
	 *         5005 账号不存在
	 */
	@Override
	public Result getUserInfo(String userId) {
		logger.info("getUserInfo userId={}", userId);
		Result result = new Result();
		if (StringUtils.isEmpty(userId)) {
			logger.info("getUserInfo userId is null");
			result.setResCode(ResultUserCode.RES_NONULL);
			return result;
		}

		TbUser user = new TbUser();
		// 设置ID参数
		user.setUserId(userId);
		// 查询用户
		List<GetUserInfoDTO> list = Collections.EMPTY_LIST;
		try {
			list = tbUserMapper.getUserinfoExample(getExample(user));
		} catch (Exception e) {
			logger.error("getUserInfo is error msg={}", e.getMessage());
			result.setResCode(ResultUserCode.RES_NO);
			return result;
		}
		if (list.size() > 0) {
			GetUserInfoDTO getUserInfoDTO = list.get(0);

			logger.info("getUserInfo is success, returnMsg={}",
					getUserInfoDTO.toString());
			if (!StringUtils.isEmpty(getUserInfoDTO.getAvatar())) {
				getUserInfoDTO.setAvatar(URL_HEAD + getUserInfoDTO.getAvatar());
			}
			result.setResCode(ResultUserCode.RES_OK);
			// 返回用户对象
			result.setResult(getUserInfoDTO);
			return result;
		} else {
			logger.info("getUserInfo is success, returnMsg is null");
			// 没有找到数据 表示用户不存在
			result.setResCode(ResultUserCode.USER_NO_USER);
			return result;
		}

	}

	/**
	 * 根据用户名或者用户手机获取用户信息
	 * 
	 * @param param
	 *            用户查询信息用户名或手机
	 * @return 0000 执行成功<br/>
	 *         2000 参数为空<br/>
	 *         1000 系统错误<br/>
	 *         5005 账号不存在
	 */
	@Override
	public Result getUser(String param) {
		logger.info("getUser param={}", param);
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			logger.info("getUser query username param is null");
			result.setResCode(ResultUserCode.RES_NONULL);
			return result;
		}

		// 封装查询条件参数
		TbUser username = new TbUser();
		username.setUsername(param);
		List<UserDTO> listusername = Collections.EMPTY_LIST;
		try {
			listusername = tbUserMapper.getUserExample(getExample(username));
		} catch (Exception e) {
			logger.error("getUser is error msg={}", e.getMessage());
			result.setResCode(ResultUserCode.RES_NO);
			return result;
		}
		if (listusername.size() > 0) {
			UserDTO usernameDTO = listusername.get(0);
			logger.info("getUser is success,returnMsg={}",
					usernameDTO.toString());
			if (!StringUtils.isEmpty(usernameDTO.getAvatar())) {
				usernameDTO.setAvatar(URL_HEAD + usernameDTO.getAvatar());
			}
			result.setResCode(ResultUserCode.RES_OK);
			// 返回用户对象
			result.setResult(usernameDTO);
			return result;
		}
		TbUser phone = new TbUser();
		phone.setPhone(param);
		List<UserDTO> listphone = Collections.EMPTY_LIST;
		try {
			listphone = tbUserMapper.getUserExample(getExample(phone));
		} catch (Exception e) {
			logger.error("getUser query phone is error msg={}", e.getMessage());
			result.setResCode(ResultUserCode.RES_NO);
			return result;
		}
		if (listphone.size() > 0) {
			UserDTO phoneDTO = listphone.get(0);
			logger.info("getUser is success, returnMsg={}", phoneDTO.toString());
			if (!StringUtils.isEmpty(phoneDTO.getAvatar())) {
				phoneDTO.setAvatar(URL_HEAD + phoneDTO.getAvatar());
			}
			result.setResCode(ResultUserCode.RES_OK);
			// 返回用户对象
			result.setResult(phoneDTO);
			return result;
		} else {
			logger.info("getUser is success, returnMsg is null");
			// 没有找到数据 表示用户不存在
			result.setResCode(ResultUserCode.USER_NO_USER);
			return result;
		}
	}

	/**
	 * 根据用户名，用户手机进行登录，
	 * 
	 * @param userOrphone
	 *            用户名或手机
	 * @param password
	 *            用户登录密码
	 * @return 0000 执行成功<br/>
	 *         2000 参数缺失<br/>
	 *         5008 密码错误<br/>
	 *         5005 用户不存在<br/>
	 *         5000 账户冻结<br/>
	 *         5004 账户注销
	 */
	@Override
	public Result getUser(String userOrphone, String password) {

		Result msg = new Result();
		if (StringUtils.isNotEmpty(userOrphone)
				&& StringUtils.isNotEmpty(password)) {

			TbUserExample example = new TbUserExample();
			example.createCriteria().andUsernameEqualTo(userOrphone);
			example.or().andPhoneEqualTo(userOrphone);
			// 查询用户
			List<UserModel> list = tbUserMapper.selectUserExample(example);
			if (list.size() > 0) {
				UserModel u = list.get(0);
				// 判断密码是否正确
				if (MD5Util.verifyPassword(password, u.getPassword())) {
					// 判断用户是否冻结
					if (BigDecimal.ZERO.compareTo(u.getStatus()) == 0) {
						msg.setResCode(ResultUserCode.USER_USER_LOCK);
						return msg;
					}
					// 判断账号是否注销
					if (BigDecimal.ONE.compareTo(u.getIslock()) == 0) {
						msg.setResCode(ResultUserCode.USER_USER_OUT);
						return msg;
					}
					UserDTO userR = new UserDTO();
					// 设置返回状态
					BeanUtils.copyProperties(u, userR);
					msg.setResult(userR);
					msg.setResCode(ResultUserCode.RES_OK);
				} else {
					// 密码错误
					msg.setResCode(ResultUserCode.USER_PASSWORD_ERROR);
				}
			} else {
				// 用户名不存在
				msg.setResCode(ResultUserCode.USER_NO_USER);
			}

		} else {
			msg.setResCode(ResultUserCode.RES_NONULL);
		}
		return msg;
	}

	/**
	 * 校验用户支付密码是否正确<br/>
	 * <b>传递加密过后数据进行处理</b>
	 * 
	 * @param userId
	 *            用户编号
	 * @param payPassword
	 *            支付密码，为加密值
	 * @return 0000 执行成功<br/>
	 *         1000 操作失败<br/>
	 *         2000 参数缺失<br/>
	 *         5005 账号不存在<br/>
	 *         5009 支付密码错误<br/>
	 *         5011 解密失败<br/>
	 *         5000 账号冻结<br/>
	 *         5004 账号注销<br/>
	 *         5010 未设置支付密码
	 */
	@Override
	public Result isExistsUserDESPPayPassword(String userId, String payPassword) {
		logger.info("isExistsUserMD5PayPassword userId ={} ,payPassword ={}",
				userId, payPassword);
		Result res = new Result();
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(payPassword)) {
			logger.info("isExistsUserDESPPayPassword userId or payPassword is null");
			res.setResCode(ResultUserCode.RES_NONULL);
			return res;
		}
		// 判断用户信息是否存在
		Result userinfo = this.getUserInfo(userId);
		if (!userinfo.getResCode().equals(ResultUserCode.RES_OK)) {
			logger.info("isExistsUserMD5PayPassword userinfo is null");
			return userinfo;
		}
		try {
			GetUserInfoDTO getUser = (GetUserInfoDTO) userinfo.getResult();
			if (getUser != null) {
				// 判断如果账号被冻结
				if (getUser.getStatus() == ResultUserCode.STATUS_NO) {
					logger.info("isExistsUserMD5PayPassword user status = 0");
					res.setResCode(ResultUserCode.USER_USER_LOCK);
					return res;
				}

				// 判断如果账号被注销
				if (getUser.getIslock() == ResultUserCode.ISLOCK_NO) {
					logger.info("isExistsUserMD5PayPassword user islock = 1");
					res.setResCode(ResultUserCode.USER_USER_OUT);
					return res;
				}
			}
			payPassword = new DESPlus().decrypt(payPassword);
		} catch (Exception e) {
			logger.error(
					"isExistsUserMD5PayPassword payPassword deciphering is error,msg={}",
					e.getMessage());
			res.setResCode(ResultUserCode.DESP_ERROR);
			return res;
		}
		res = this.isExistsUserPayPassword(userId, payPassword);
		logger.info("isExistsUserMD5PayPassword payPassword deciphering is success");
		return res;
	}

	/**
	 * 校验用户支付密码是否正确
	 * 
	 * @param userId
	 *            用户编号
	 * @param payPassword
	 *            支付密码，为加密原始值
	 * @return 0000 执行成功<br/>
	 *         1000 操作失败<br/>
	 *         2000 参数缺失<br/>
	 *         5005 账号不存在<br/>
	 *         5009 支付密码错误<br/>
	 *         5010 未设置支付密码
	 */
	@Override
	public Result isExistsUserPayPassword(String userId, String payPassword) {
		logger.info("isExistsUserPayPassword userId ={} ,payPassword ={}",
				userId, payPassword);
		Result res = new Result();
		if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(payPassword)) {

			User user = null;
			try {
				user = tbUserMapper.selectByPrimaryKey(userId);
			} catch (Exception e) {
				logger.error("isExistsUserPayPassword is error msg={}",
						e.getMessage());
				res.setResCode(ResultUserCode.RES_NO);
				return res;
			}
			if (null == user) {
				logger.info("isExistsUserPayPassword user is null");
				res.setResCode(ResultUserCode.USER_NO_USER);
			} else {

				if (StringUtils.isNotEmpty(user.getPaypassword())) {
					if (MD5Util.verifyPassword(payPassword,
							user.getPaypassword())) {
						logger.info("isExistsUserPayPassword payPassword validate success");
						res.setResCode(ResultUserCode.RES_OK);
					} else {
						logger.info("isExistsUserPayPassword payPassword validate error");
						res.setResCode(ResultUserCode.USER_PLYPASSWORD_ERROR);
					}
				} else {
					logger.info("isExistsUserPayPassword user  payPassword is null");
					res.setResCode(ResultUserCode.USER_PLYPASSWORD_NULL);
				}
			}
		} else {
			logger.info("isExistsUserPayPassword userId or payPassword is null");
			res.setResCode(ResultUserCode.RES_NONULL);
		}
		return res;
	}

	/**
	 * 用户编号获取用户手机号和用户邮箱
	 * 
	 * @param userIds
	 *            用户编号
	 * @return 0000 操作成功 2000 参数缺失 1000 操作失败
	 */
	@Override
	public Result getUserPhoneMail(List<String> userIds) {
		logger.info("getUserPhoneMail userIds={}", userIds);
		Result result = new Result();
		if (userIds.size() == 0) {
			logger.info("getUserPhoneMail userIds.size is 0");
			result.setResCode(ResultCode.RES_NONULL);
			return result;
		}
		TbUserExample example = new TbUserExample();
		TbUserExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdIn(userIds);
		List<PhoneMailDTO> phoneMailDTOs = Collections.EMPTY_LIST;
		try {
			phoneMailDTOs = tbUserMapper.getUserIdPhoneMail(example);
		} catch (Exception e) {
			logger.error(
					"getUserPhoneMail tbUserMapper.getUserIdPhoneMail is error",
					e.getMessage());
			result.setResCode(ResultCode.RES_NO);
			return result;
		}
		logger.info("getUserPhoneMail tbUserMapper.getUserIdPhoneMail is success");
		result.setResCode(ResultCode.RES_OK);
		result.setResult(phoneMailDTOs);
		return result;
	}

	/**
	 *板块来源修改用户状态
	 * @param userId 用户ID
	 * @param blockSource 备注内容
	 * @return
     */
	@Override
	public Result updateUserBlockSource(String userId,String blockSource) {
		logger.info("updateUserBlockSource userId={}",userId);
		Result result=new Result();
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(blockSource)){
			logger.error("updateUserBlockSource userId is null");
			result.setResCode(ResultCode.RES_NONULL);
			return result;
		}

		UserinfoExample example=new UserinfoExample();
		UserinfoExample.Criteria criteria=example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		Userinfo userinfo=new Userinfo();
		if(StringUtils.isNotEmpty(blockSource)){
			userinfo.setBlockSource(blockSource);
		}
		try {
			int rows=userinfoMapper.updateByExampleSelective(userinfo,example);
			if(rows>0){
				result.setResCode(ResultCode.RES_OK);
				return result;
			}
			result.setResCode(ResultCode.USER_NO_USER);
			return result;
		}catch (Exception e){
			logger.error("updateUserBlockSource is error,msg={}",e.getMessage());
			result.setResCode(ResultCode.RES_NO);
			return result;
		}
	}

	/**
	 * 手机号身份证用户信息反查
	 * @param phones 手机号集合
	 * @param cards 身份证号集合
     * @return
     */
	@Override
	public Result counterUser(List<String> phones, List<String> cards) {
		logger.info("counterUser phones={},cards={}",phones,cards);
		Result result=new Result();
		if(phones==null|| phones.size()==0 || cards==null || cards.size()==0){
			logger.error("counterUser phones or cards or  is null");
			result.setResCode(ResultCode.RES_NONULL);
			return result;
		}
		try {
			UserinfoExample example = new UserinfoExample();

			for (String phone : phones) {
				for (String card : cards) {
					UserinfoExample.Criteria criteria = example.or();
					criteria.andPhoneEqualTo(phone);
					criteria.andCardIdEqualTo(card);
				}
			}
			List<Userinfo> userinfos = userinfoMapper.selectByExample(example);
			if (userinfos.size() == 0) {
				logger.error("counterUser userinfos.size() is 0");
				result.setResCode(ResultCode.USER_NO_USER);
				return result;
			}
			result.setResCode(ResultCode.RES_OK);
			result.setResult(userinfos);
			logger.info("counterUser is success");
			return result;
		}catch (Exception e){
			logger.error("counterUser is error,msg={}",e.getMessage());
			result.setResCode(ResultCode.RES_NO);
			return result;
		}
	}

	/**
	 * 使用用户名和密码进行校验用户信息<br/>
	 * 当用户被冻结或不存在时返回对应提示信息和错误编号
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 当用户属于正常状态时返回用户ID<br/>
	 */
	/*
	 * @Override public Result getNameUser(String username, String password) {
	 * Result res = new Result(); // 判断用户名和密码不为空 if
	 * (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) { //
	 * 调用内部查询方法 res = getNamePhoneUser(username, null, password); } else {
	 * res.setResCode(ResultUserCode.RES_NONULL); } return res; }
	 */

	/**
	 * 使用 手机号和密码进行校验用户信息<br/>
	 * 当用户被冻结或不存在时返回对应提示信息和错误编号
	 * 
	 * @param phone
	 *            手机号
	 * @param password
	 *            密码
	 * @return 当用户属于正常状态时返回用户ID<br/>
	 */
	/*
	 * @Override public Result getPhoneUser(String phone, String password) {
	 * Result msg = new Result(); if (StringUtils.isNotEmpty(phone) &&
	 * StringUtils.isNotEmpty(password)) { // 调用内部查询判断手机号和密码 msg =
	 * getNamePhoneUser(null, phone, password); } else {
	 * msg.setResCode(ResultUserCode.RES_NONULL); } return msg; }
	 */

	/**
	 * 使用用户名和密码进行获取用户信息<br/>
	 * 当用户属于正常状态时返回用户信息<br/>
	 * 当用户不存在时返回对应错误编号
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码（未加密）
	 * @return
	 */
	/*
	 * @Override public Result getNameUserInfo(String userName, String password)
	 * { Result msg = new Result(); if (StringUtils.isNotEmpty(userName) &&
	 * StringUtils.isNotEmpty(password)) { TbUser user = new TbUser(); //
	 * 设置用户名作为查询条件 user.setUsername(userName); TbUserExample example =
	 * getExample(user); // 查询用户 List<UserModel> list =
	 * tbUserMapper.selectUserExample(example); if (list.size() > 0) { UserModel
	 * u = list.get(0); // 判断密码是否正确 if (MD5Util.verifyPassword(password,
	 * u.getPassword())) { // 判断用户是否冻结 if
	 * (BigDecimal.ZERO.compareTo(u.getStatus()) == 0) {
	 * msg.setResCode(ResultUserCode.USER_USER_LOCK); return msg; } // 判断账号是否注销
	 * if (BigDecimal.ONE.compareTo(u.getIslock()) == 0) {
	 * msg.setResCode(ResultUserCode.USER_USER_OUT); return msg; } UserDTO userR
	 * = new UserDTO(); // 设置返回状态 BeanUtils.copyProperties(u, userR);
	 * msg.setResult(userR); msg.setResCode(ResultUserCode.RES_OK); } else { //
	 * 密码错误 msg.setResCode(ResultUserCode.USER_PASSWORD_ERROR); } } else { //
	 * 用户名不存在 msg.setResCode(ResultUserCode.USER_NO_USERNAME); }
	 * 
	 * } else { msg.setResCode(ResultUserCode.RES_NONULL); } return msg; }
	 */
	/**
	 * 使用手机号和密码进行获取用户信息<br/>
	 * 当用户属于正常状态时返回用户信息<br/>
	 * 当手机号不存在时返回对应错误编号
	 * 
	 * @param phone
	 *            手机号
	 * @param password
	 *            密码（未加密）
	 * @return
	 */
	/*
	 * @Override public Result getPhoneUserInfo(String phone, String password) {
	 * Result msg = new Result(); if (StringUtils.isNotEmpty(phone) &&
	 * StringUtils.isNotEmpty(password)) { TbUser user = new TbUser();
	 * user.setPhone(phone); TbUserExample example = getExample(user);
	 * List<UserModel> list = tbUserMapper.selectUserExample(example); if
	 * (list.size() > 0) { UserModel u = list.get(0); if
	 * (MD5Util.verifyPassword(password, u.getPassword())) { // 判断是否冻结 if
	 * (BigDecimal.ZERO.compareTo(u.getStatus()) == 0) {
	 * msg.setResCode(ResultUserCode.USER_USER_LOCK); return msg; } // 判断账号是否注销
	 * if (BigDecimal.ONE.compareTo(u.getIslock()) == 0) {
	 * msg.setResCode(ResultUserCode.USER_USER_OUT); return msg; } UserDTO userR
	 * = new UserDTO(); // 设置返回状态 BeanUtils.copyProperties(u, userR); //
	 * 设置返回的用户信息 msg.setResult(userR); msg.setResCode(ResultUserCode.RES_OK); }
	 * else { // 密码错误 msg.setResCode(ResultUserCode.USER_PASSWORD_ERROR); } }
	 * else { // 没有找到数据 表示手机号不存在 msg.setResCode(ResultUserCode.USER_NO_PHONE); }
	 * 
	 * } else { msg.setResCode(ResultUserCode.RES_NONULL); } return msg; }
	 */

	/**
	 * 使用用户ID获取用户信息<br/>
	 * 当用户存在返回用户信息<br/>
	 * 当用户不存在时返回对应错误编号
	 * 
	 * @param userId
	 *            用户编号
	 * @return
	 */
	/*
	 * @Override public Result getIdUser(String userId) { Result res = new
	 * Result(); if (StringUtils.isNotEmpty(userId)) { TbUser user = new
	 * TbUser(); // 设置ID参数 user.setUserId(userId); // 查询用户 List<UserInfoDTO>
	 * list = tbUserMapper.selectUserInfoExample(getExample(user)); if
	 * (list.size() > 0) { res.setResCode(ResultUserCode.RES_OK); // 返回用户对象
	 * res.setResult(list.get(0)); } else { // 没有找到数据 表示用户不存在
	 * res.setResCode(ResultUserCode.USER_NO_USER); } } else {
	 * res.setResCode(ResultUserCode.RES_NONULL); } return res; }
	 */

	/**
	 * 根据用户名或手机号及密码获取可以进行登录的账号ID
	 * 
	 * @param username
	 *            用户名
	 * @param phone
	 *            手机号
	 * @param password
	 *            密码
	 * @return
	 */
	/*
	 * private Result getNamePhoneUser(String username, String phone, String
	 * password) { Result msg = new Result();
	 * msg.setResCode(ResultUserCode.RES_NONULL); // 判断用户名和密码不为空 if
	 * (StringUtils.isNotEmpty(password)) { TbUser user = new TbUser(); if
	 * (StringUtils.isNotEmpty(username)) { // 构造查询用户名
	 * user.setUsername(username); } else if (StringUtils.isNotEmpty(phone)) { //
	 * 构造查询手机号 user.setPhone(phone); } else { return msg; } TbUserExample
	 * example = getExample(user); List<TbUser> list =
	 * tbUserMapper.selectUserIdExample(example); if (list.size() > 0) { user =
	 * list.get(0); // 判断密码是否正确 if (MD5Util.verifyPassword(password,
	 * user.getPassword())) { // 判断用户是否冻结 if (0 == user.getStatus()) {
	 * msg.setResCode(ResultUserCode.USER_USER_LOCK); return msg; } //
	 * 判断用户账号是否已注销 if (1 == user.getIslock()) {
	 * msg.setResCode(ResultUserCode.USER_USER_OUT); return msg; } // 设置返回用户编号
	 * msg.setResult(user.getUserId()); msg.setResCode(ResultUserCode.RES_OK); }
	 * else { msg.setResCode(ResultUserCode.USER_PASSWORD_ERROR); } } else { if
	 * (StringUtils.isNotEmpty(phone)) { // 设置手机号不存在
	 * msg.setResCode(ResultUserCode.USER_NO_PHONE); } else { // 设置用户名不存在
	 * msg.setResCode(ResultUserCode.USER_NO_USERNAME); } } } return msg; }
	 */

	/*
	 * 
	 * @Override public Result getNameUser(String username) { Result res = new
	 * Result(); if (StringUtils.isNotEmpty(username)) { TbUser user = new
	 * TbUser(); // 设置ID参数 user.setUsername(username); // 查询用户 List<UserInfoDTO>
	 * list = tbUserMapper.selectUserInfoExample(getExample(user)); if
	 * (list.size() > 0) { res.setResCode(ResultUserCode.RES_OK); // 返回用户对象
	 * res.setResult(list.get(0)); } else { // 没有找到数据 表示用户不存在
	 * res.setResCode(ResultUserCode.USER_NO_USER); } } else {
	 * res.setResCode(ResultUserCode.RES_NONULL); } return res; }
	 */

	/**
	 * 封装查询条件 ，密码自动进行MD5码加密
	 * 
	 * @param user
	 *            用户对象
	 * @return 返回封装的查询对象 TbUserExample
	 */
	private TbUserExample getExample(TbUser user) {
		TbUserExample example = new TbUserExample();
		TbUserExample.Criteria criteria = example.createCriteria();
		if (null != user) {
			// 构造用户编号条件
			if (StringUtils.isNotEmpty(user.getUserId())) {
				criteria.andUserIdEqualTo(user.getUserId());
			}
			// 邮箱不为空时增加邮箱等于值的条件
			if (StringUtils.isNotEmpty(user.getEmail())) {
				criteria.andEmailEqualTo(user.getEmail());
			}
			// 用户名不为空时增加用户名等于值的条件
			if (StringUtils.isNotEmpty(user.getUsername())) {
				criteria.andUsernameEqualTo(user.getUsername());
			}
			// 电话不为空时增加电话等于值的条件
			if (StringUtils.isNotEmpty(user.getPhone())) {
				criteria.andPhoneEqualTo(user.getPhone());
			}
			// 密码不为空时增加密码等于加密值的条件
			if (StringUtils.isNotEmpty(user.getPassword())) {
				criteria.andPasswordEqualTo(MD5Util.getMD5ofStr(user
						.getPassword().trim()));
			}
		}
		return example;
	}

}
