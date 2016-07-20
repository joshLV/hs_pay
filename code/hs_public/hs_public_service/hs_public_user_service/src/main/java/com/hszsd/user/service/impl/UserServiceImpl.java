package com.hszsd.user.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.hszsd.common.util.DESPlus;
import com.hszsd.common.util.string.StringUtil;
import com.hszsd.entity.User;
import com.hszsd.user.dto.GetUserInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hszsd.common.util.MD5Util;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.ReturnMsg;
import com.hszsd.user.dao.TbUserMapper;
import com.hszsd.user.dto.UserDTO;
import com.hszsd.user.dto.UserInfoDTO;
import com.hszsd.user.po.TbUser;
import com.hszsd.user.po.TbUserExample;
import com.hszsd.user.po.UserModel;
import com.hszsd.user.service.UserService;
import com.hszsd.user.util.ResultUserCode;

/**
 * 用户信息接口实现类<br/>
 * 贵州合石电子商务有限公司
 * 
 * @author 艾伍
 * @date 2016-04-25
 * @version 1.0.0
 * 
 */
@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	public TbUserMapper tbUserMapper;

	/**
	 * 验证系统中用户名是否存在
	 * 
	 * @param username
	 *            用户名
	 * @return success 为true表示存在<br/>
	 *         为false表示不存在
	 */
	@Override
	public ReturnMsg isExistsUserName(String username) {
		ReturnMsg msg = new ReturnMsg();
		// 判断是否有效 只进行非空判断
		if (StringUtils.hasLength(username)) {
			// 封装查询条件
			TbUser user = new TbUser();
			user.setUsername(username);
			// 设置返回状态
			msg.setSuccess(getUserCount(user) > 0);
		}
		return msg;
	}

	/**
	 * 验证系统中手机号是否存在
	 * 
	 * @param phone
	 *            手机号
	 * @return success 为true表示存在<br/>
	 *         为false表示不存在
	 */
	@Override
	public ReturnMsg isExistsPhone(String phone) {
		ReturnMsg msg = new ReturnMsg();
		// 判断是否有效 只进行非空判断，不做正则表达式判断
		if (StringUtils.hasLength(phone)) {
			// 封装查询条件
			TbUser user = new TbUser();
			user.setPhone(phone);
			// 设置返回状态
			msg.setSuccess(getUserCount(user) > 0);
		}
		return msg;
	}

	/**
	 * 验证系统中邮箱是否存在
	 * 
	 * @param email
	 *            邮箱
	 * @return success 为true表示存在<br/>
	 *         为false表示不存在
	 */
	@Override
	public ReturnMsg isExistsEmail(String email) {
		ReturnMsg msg = new ReturnMsg();
		// 判断是否有效 只进行非空判断，不做正则表达式判断
		if (StringUtils.hasLength(email)) {
			// 封装查询条件
			TbUser user = new TbUser();
			user.setEmail(email);
			// 设置返回状态
			msg.setSuccess(getUserCount(user) > 0);
		}
		return msg;
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
	 *         001成功<br/>
	 *         002 失败<br/>
	 *         002_01 参数缺失<br/>
	 *         002_02 系统错误<br/>
	 *         100_01 用户名不存在<br/>
	 *         101_01 用户被冻结<br/>
	 *         101_02 用户已注销 <br/>
	 *         102_01 密码错误
	 */
	@Override
	public Result getNameUser(String username, String password) {
		Result res = new Result();
		// 判断用户名和密码不为空
		if (StringUtils.hasLength(username) && StringUtils.hasLength(password)) {
			// 调用内部查询方法
			res = getNamePhoneUser(username, null, password);
		} else {
			res.setCode(ResultUserCode.RES_NONULL);
		}
		return res;
	}

	/**
	 * 使用 手机号和密码进行校验用户信息<br/>
	 * 当用户被冻结或不存在时返回对应提示信息和错误编号
	 * 
	 * @param phone
	 *            手机号
	 * @param password
	 *            密码
	 * @return 当用户属于正常状态时返回用户ID<br/>
	 *         001成功<br/>
	 *         002 失败 <br/>
	 *         002_01 参数缺失 <br/>
	 *         002_02 系统错误<br/>
	 *         100_02 手机号不存在<br/>
	 *         101_01 用户被冻结<br/>
	 *         101_02 用户已注销<br/>
	 *         102_01 密码错误
	 * 
	 * 
	 */
	@Override
	public Result getPhoneUser(String phone, String password) {
		Result msg = new Result();
		if (StringUtils.hasLength(phone) && StringUtils.hasLength(password)) {
			// 调用内部查询判断手机号和密码
			msg = getNamePhoneUser(null, phone, password);
		} else {
			msg.setCode(ResultUserCode.RES_NONULL);
		}
		return msg;
	}


	/**
	 * 使用用户名和密码进行获取用户信息<br/>
	 * 当用户属于正常状态时返回用户信息<br/>
	 * 当用户不存在时返回对应错误编号
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码（未加密）
	 * @return 001成功<br/>
	 *         002 失败 <br/>
	 *         002_01 参数缺失 <br/>
	 *         002_02 系统错误<br/>
	 *         100_01 用户名不存在<br/>
	 *         101_01 用户被冻结<br/>
	 *         101_02 用户已注销<br/>
	 *         102_01 密码错误
	 */
	@Override
	public Result getNameUserInfo(String userName, String password) {
		Result msg = new Result();
		if (StringUtils.hasLength(userName) && StringUtils.hasLength(password)) {
			TbUser user = new TbUser();
			// 设置用户名作为查询条件
			user.setUsername(userName);
			TbUserExample example = getExample(user);
			// 查询用户
			List<UserModel> list = tbUserMapper.selectUserExample(example);
			if (list.size() > 0) {
				UserModel u = list.get(0);
				// 判断密码是否正确
				if (MD5Util.verifyPassword(password, u.getPassword())) {
					// 判断用户是否冻结
					if (BigDecimal.ZERO.compareTo(u.getStatus()) == 0) {
						msg.setCode(ResultUserCode.USER_USER_LOCK);
						return msg;
					}
					// 判断账号是否注销
					if (BigDecimal.ONE.compareTo(u.getIslock()) == 0) {
						msg.setCode(ResultUserCode.USER_USER_OUT);
						return msg;
					}
					UserDTO userR = new UserDTO();
					// 设置返回状态
					BeanUtils.copyProperties(u, userR);
					msg.setResult(userR);
					msg.setCode(ResultUserCode.RES_OK);
				} else {
					// 密码错误
					msg.setCode(ResultUserCode.USER_PASSWORD_ERROR);
				}
			} else {
				// 用户名不存在
				msg.setCode(ResultUserCode.USER_NO_USERNAME);
			}

		} else {
			msg.setCode(ResultUserCode.RES_NONULL);
		}
		return msg;
	}

	/**
	 * 使用手机号和密码进行获取用户信息<br/>
	 * 当用户属于正常状态时返回用户信息<br/>
	 * 当手机号不存在时返回对应错误编号
	 * 
	 * @param phone
	 *            手机号
	 * @param password
	 *            密码（未加密）
	 * @return 001成功 <br/>
	 *         002 失败 <br/>
	 *         002_01 参数缺失 <br/>
	 *         002_02 系统错误<br/>
	 *         100_02 手机号不存在<br/>
	 *         101_01 用户被冻结<br/>
	 *         101_02 用户已注销<br/>
	 *         102_01 密码错误
	 */
	@Override
	public Result getPhoneUserInfo(String phone, String password) {
		Result msg = new Result();
		if (StringUtils.hasLength(phone) && StringUtils.hasLength(password)) {
			TbUser user = new TbUser();
			user.setPhone(phone);
			TbUserExample example = getExample(user);
			List<UserModel> list = tbUserMapper.selectUserExample(example);
			if (list.size() > 0) {
				UserModel u = list.get(0);
				if (MD5Util.verifyPassword(password, u.getPassword())) {
					// 判断是否冻结
					if (BigDecimal.ZERO.compareTo(u.getStatus()) == 0) {
						msg.setCode(ResultUserCode.USER_USER_LOCK);
						return msg;
					}
					// 判断账号是否注销
					if (BigDecimal.ONE.compareTo(u.getIslock()) == 0) {
						msg.setCode(ResultUserCode.USER_USER_OUT);
						return msg;
					}
					UserDTO userR = new UserDTO();
					// 设置返回状态
					BeanUtils.copyProperties(u, userR);
					// 设置返回的用户信息
					msg.setResult(userR);
					msg.setCode(ResultUserCode.RES_OK);
				} else {
					// 密码错误
					msg.setCode(ResultUserCode.USER_PASSWORD_ERROR);
				}
			} else {
				// 没有找到数据 表示手机号不存在
				msg.setCode(ResultUserCode.USER_NO_PHONE);
			}

		} else {
			msg.setCode(ResultUserCode.RES_NONULL);
		}
		return msg;
	}

	/**
	 * 使用用户ID获取用户信息<br/>
	 * 当用户存在返回用户信息<br/>
	 * 当用户不存在时返回对应错误编号
	 * 
	 * @param userId
	 *            用户编号
	 * @return 001成功<br/>
	 *         002 失败 <br/>
	 *         002_01 参数缺失 <br/>
	 *         002_02 系统错误<br/>
	 *         100 账号不存在
	 */
	@Override
	public Result getIdUser(String userId) {
		Result res = new Result();
		if (StringUtils.hasLength(userId)) {
			TbUser user = new TbUser();
			// 设置ID参数
			user.setUserId(userId);
			// 查询用户
			List<UserInfoDTO> list = tbUserMapper.selectUserInfoExample(getExample(user));
			if (list.size() > 0) {
				res.setCode(ResultUserCode.RES_OK);
				// 返回用户对象
				res.setResult(list.get(0));
			} else {
				// 没有找到数据 表示用户不存在
				res.setCode(ResultUserCode.USER_NO_USER);
			}
		} else {
			res.setCode(ResultUserCode.RES_NONULL);
		}
		return res;
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
	private Result getNamePhoneUser(String username, String phone, String password) {
		Result msg = new Result();
		msg.setCode(ResultUserCode.RES_NONULL);
		// 判断用户名和密码不为空
		if (StringUtils.hasLength(password)) {
			TbUser user = new TbUser();
			if (StringUtils.hasLength(username)) {
				// 构造查询用户名
				user.setUsername(username);
			} else if (StringUtils.hasLength(phone)) {
				// 构造查询手机号
				user.setPhone(phone);
			} else {
				return msg;
			}
			TbUserExample example = getExample(user);
			List<TbUser> list = tbUserMapper.selectUserIdExample(example);
			if (list.size() > 0) {
				user = list.get(0);
				// 判断密码是否正确
				if (MD5Util.verifyPassword(password, user.getPassword())) {
					// 判断用户是否冻结
					if (0 == user.getStatus()) {
						msg.setCode(ResultUserCode.USER_USER_LOCK);
						return msg;
					}
					// 判断用户账号是否已注销
					if (1 == user.getIslock()) {
						msg.setCode(ResultUserCode.USER_USER_OUT);
						return msg;
					}
					// 设置返回用户编号
					msg.setResult(user.getUserId());
					msg.setCode(ResultUserCode.RES_OK);
				} else {
					msg.setCode(ResultUserCode.USER_PASSWORD_ERROR);
				}
			} else {
				if (StringUtils.hasLength(phone)) {
					// 设置手机号不存在
					msg.setCode(ResultUserCode.USER_NO_PHONE);
				} else {
					// 设置用户名不存在
					msg.setCode(ResultUserCode.USER_NO_USERNAME);
				}
			}
		}
		return msg;
	}

	/**
	 * 封装查询条件 ，密码自动进行MD5码加密
	 * 
	 * @param user
	 *            用户对象
	 * @return 返回封装的查询对象 TbUserExample
	 */
	private TbUserExample getExample(TbUser user) {
		TbUserExample example = new TbUserExample();
		TbUserExample.Criteria criteria=example.createCriteria();
		if (null != user) {
			// 构造用户编号条件
			if (StringUtils.hasLength(user.getUserId())) {
				criteria.andUserIdEqualTo(user.getUserId());
			}
			// 邮箱不为空时增加邮箱等于值的条件
			if (StringUtils.hasLength(user.getEmail())) {
				criteria.andEmailEqualTo(user.getEmail());
			}
			// 用户名不为空时增加用户名等于值的条件
			if (StringUtils.hasLength(user.getUsername())) {
				criteria.andUsernameEqualTo(user.getUsername());
			}
			// 电话不为空时增加电话等于值的条件
			if (StringUtils.hasLength(user.getPhone())) {
				criteria.andPhoneEqualTo(user.getPhone());
			}
			// 密码不为空时增加密码等于加密值的条件
			if (StringUtils.hasLength(user.getPassword())) {
				criteria.andPasswordEqualTo(MD5Util.getMD5ofStr(user.getPassword().trim()));
			}
		}
		return example;
	}


	/**
	 * 校验用户支付密码是否正确<br/>
	 * <b>传递加密过后数据进行处理</b>
	 * @param userId
	 *            用户编号
	 * @param payPassword
	 *            支付密码，为加密值
	 * @return
	 */
	public Result isExistsUserMD5PayPassword(String userId, String payPassword){
		logger.info("isExistsUserMD5PayPassword userId ={} ,payPassword ={}",userId,payPassword);
		Result res = new Result();
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(payPassword)) {
			logger.info("isExistsUserMD5PayPassword userId or payPassword is null");
			res.setCode(ResultUserCode.RES_NONULL);
			return res;
		}
		try {
			payPassword= new DESPlus().decrypt(payPassword);
		} catch (Exception e) {
			logger.error("isExistsUserMD5PayPassword payPassword deciphering is error,msg={}",e.getMessage());
			res.setCode(ResultUserCode.RES_NO);
			return  res;
		}
		res=this.isExistsUserPayPassword(userId, payPassword);
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
	 * @return
	 */
	@Override
	public Result isExistsUserPayPassword(String userId, String payPassword) {
		logger.info("isExistsUserPayPassword userId ={} ,payPassword ={}",userId,payPassword);
		Result res = new Result();
		if (StringUtils.hasLength(userId) && StringUtils.hasLength(payPassword)) {

			User user =null;
			try {
				user=tbUserMapper.selectByPrimaryKey(userId);
			}catch (Exception e){
				logger.error("isExistsUserPayPassword is error msg={}",e.getMessage());
				res.setCode(ResultUserCode.RES_NO);
				return  res;
			}
			if (null == user) {
				logger.info("isExistsUserPayPassword user is null");
				res.setCode(ResultUserCode.USER_NO_USER);
			} else {

				if (StringUtils.hasLength(user.getPaypassword())) {
					if (MD5Util.verifyPassword(payPassword, user.getPaypassword())) {
						logger.info("isExistsUserPayPassword payPassword validate success");
						res.setCode(ResultUserCode.RES_OK);
					} else {
						logger.info("isExistsUserPayPassword payPassword validate error");
						res.setCode(ResultUserCode.USER_PLYPASSWORD_ERROR);
					}
				} else {
					logger.info("isExistsUserPayPassword user  payPassword is null");
					res.setCode(ResultUserCode.USER_PLYPASSWORD_NULL);
				}
			}
		} else {
			logger.info("isExistsUserPayPassword userId or payPassword is null");
			res.setCode(ResultUserCode.RES_NONULL);
		}
		return res;
	}



	@Override
	public Result getNameUser(String username) {
		Result res = new Result();
		if (StringUtils.hasLength(username)) {
			TbUser user = new TbUser();
			// 设置ID参数
			user.setUsername(username);
			// 查询用户
			List<UserInfoDTO> list = tbUserMapper.selectUserInfoExample(getExample(user));
			if (list.size() > 0) {
				res.setCode(ResultUserCode.RES_OK);
				// 返回用户对象
				res.setResult(list.get(0));
			} else {
				// 没有找到数据 表示用户不存在
				res.setCode(ResultUserCode.USER_NO_USER);
			}
		} else {
			res.setCode(ResultUserCode.RES_NONULL);
		}
		return res;
	}

	public Result getUser(String userOrphone, String password) {

		Result msg = new Result();
		if (StringUtils.hasLength(userOrphone) && StringUtils.hasLength(password)) {

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
						msg.setCode(ResultUserCode.USER_USER_LOCK);
						return msg;
					}
					// 判断账号是否注销
					if (BigDecimal.ONE.compareTo(u.getIslock()) == 0) {
						msg.setCode(ResultUserCode.USER_USER_OUT);
						return msg;
					}
					UserDTO userR = new UserDTO();
					// 设置返回状态
					BeanUtils.copyProperties(u, userR);
					msg.setResult(userR);
					msg.setCode(ResultUserCode.RES_OK);
				} else {
					// 密码错误
					msg.setCode(ResultUserCode.USER_PASSWORD_ERROR);
				}
			} else {
				// 用户名不存在
				msg.setCode(ResultUserCode.USER_NO_USER);
			}

		} else {
			msg.setCode(ResultUserCode.RES_NONULL);
		}
		return msg;
	}


	/**
	 * 根据用户ID获取用户信息
	 * @param userId 用户ID
	 * @return 用户详细信息
	 */
	@Override
	public Result getUserInfo(String userId) {
		logger.info("getUserInfo userId={}",userId);
		Result result=new Result();
		if(StringUtils.isEmpty(userId)){
			logger.info("getUserInfo userId is null");
			result.setCode(ResultUserCode.RES_NONULL);
			return result;
		}
		TbUser user = new TbUser();
		// 设置ID参数
		user.setUserId(userId);
		// 查询用户
		List<GetUserInfoDTO> list = Collections.EMPTY_LIST;
		try {
			list=tbUserMapper.getUserinfoExample(getExample(user));
		}catch (Exception e){
			logger.error("getUserInfo is error msg={}",e.getMessage());
			result.setCode(ResultUserCode.RES_NO);
			return result;
		}
		if (list.size() > 0) {
			logger.info("getUserInfo is success, returnMsg={}",list.get(0).toString());
			result.setCode(ResultUserCode.RES_OK);
			// 返回用户对象
			result.setResult(list.get(0));
			return result;
		} else {
			logger.info("getUserInfo is success, returnMsg is null");
			// 没有找到数据 表示用户不存在
			result.setCode(ResultUserCode.USER_NO_USER);
			return result;
		}

	}

	/**
	 * 根据用户名或者用户手机获取用户信息
	 * @param param 用户查询信息用户名或手机
	 * @return 查询用户信息结果
	 */
	@Override
	public Result getUser(String param) {
		logger.info("getUser param={}",param);
		Result result=new Result();
		if(StringUtils.isEmpty(param)){
			logger.info("getUser query username param is null");
			result.setCode(ResultUserCode.RES_NONULL);
		}

		//封装查询条件参数
		TbUser username = new TbUser();
		username.setUsername(param);
		List<UserDTO> listusername=Collections.EMPTY_LIST;
		try {
			listusername=tbUserMapper.getUserExample(getExample(username));
		}catch (Exception e){
			e.printStackTrace();
			logger.error("getUser is error msg={}",e.getMessage());
			result.setCode(ResultUserCode.RES_NO);
			return result;
		}
		if (listusername.size() > 0) {
			logger.info("getUser is success,returnMsg={}",listusername.get(0).toString());
			result.setCode(ResultUserCode.RES_OK);
			// 返回用户对象
			result.setResult(listusername.get(0));
			return result;
		}
		TbUser phone=new TbUser();
		phone.setPhone(param);
		List<UserDTO> listphone= Collections.EMPTY_LIST;
		try {
			listphone = tbUserMapper.getUserExample( getExample(phone));
		}catch (Exception e){
			logger.error("getUser query phone is error msg={}",e.getMessage());
			result.setCode(ResultUserCode.RES_NO);
			return result;
		}
		if (listphone.size() > 0) {
			logger.info("getUser is success, returnMsg={}",listphone.get(0).toString());
			result.setCode(ResultUserCode.RES_OK);
			// 返回用户对象
			result.setResult(listphone.get(0));
			return result;
		}else {
			logger.info("getUser is success, returnMsg is null");
			// 没有找到数据 表示用户不存在
			result.setCode(ResultUserCode.USER_NO_USER);
			return result;
		}
	}


}
