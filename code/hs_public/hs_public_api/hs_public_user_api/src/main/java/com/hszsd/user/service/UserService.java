package com.hszsd.user.service;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ReturnMsg;



/**
 * 用户信息接口<br/>
 * 贵州合石电子商务有限公司
 * 
 * @author 艾伍
 * @date 2016-04-25
 * @version 1.0.0
 * 
 */
public interface UserService {
	

	/**
	 * 验证系统中用户名是否存在
	 * 
	 * @param username
	 *            用户名
	 * @return success 为true表示存在<br/>
	 *         为false表示不存在
	 */
	public ReturnMsg isExistsUserName( String username);

	/**
	 * 验证系统中手机号是否存在
	 * 
	 * @param phone
	 *            手机号
	 * @return success 为true表示存在<br/>
	 *         为false表示不存在
	 */
	public ReturnMsg isExistsPhone( String phone);

	/**
	 * 验证系统中邮箱是否存在
	 * 
	 * @param email
	 *            邮箱
	 * @return success 为true表示存在<br/>
	 *         为false表示不存在
	 */
	public ReturnMsg isExistsEmail(String email);
	
	/**
	 * 根据用户名，用户手机进行登录，
	 * @param userOrphone 用户名或手机
	 * @param password 用户登录密码
	 * @return 当用户属于正常状态时返回用户User<br/>
	 *         001成功<br/>
	 *         002 失败<br/>
	 *         002_01 参数缺失<br/>
	 *         002_02 系统错误<br/>
	 *         100_01 用户名不存在<br/>
	 *         100_02 手机号不存在<br/>
	 *         101_01 用户被冻结<br/>
	 *         101_02 用户已注销 <br/>
	 *         102_01 密码错误
	 */
	public Result getUser(String userOrphone,String password);
	

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
	public Result getNameUser(String username, String password);

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
	public Result getPhoneUser(String phone, String password);

	/**
	 * 使用用户ID、原密码修改现有密码，新密码长度大于等于6
	 * 
	 * @param userId
	 *            用户编号
	 * @param oldPassword
	 *            用户原密码（未加密）
	 * @param newPassword
	 *            用户新密码（未加密）
	 * @return 001成功 <br/>
	 *         002 失败 <br/>
	 *         002_01 参数缺失 <br/>
	 *         002_02 系统错误<br/>
	 *         100账号不存在<br/>
	 *         102_01 密码错误<br/>
	 *         104_01新密码长度不足
	 */
/*	@Deprecated
	public Result updatePassword(String userId, String oldPassword,
			String newPassword);*/

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
	public Result getNameUserInfo(String userName, String password);

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
	public Result getPhoneUserInfo(String phone, String password);

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
	public Result getIdUser(String userId);
	

	/**
	 * 使用用户名获取用户信息<br/>
	 * 当用户存在返回用户信息<br/>
	 * 当用户不存在时返回对应错误编号
	 * 
	 * @param username
	 *            用户编号
	 * @return 001成功<br/>
	 *         002 失败 <br/>
	 *         002_01 参数缺失 <br/>
	 *         002_02 系统错误<br/>
	 *         100 账号不存在
	 */
	public Result getNameUser(String username);

	/**
	 * 校验用户支付密码是否正确
	 * 
	 * @param userId
	 *            用户编号
	 * @param plyPassword
	 *            支付密码，为加密原始值
	 * @return 001成功<br/>
	 *         002_01 参数缺失 <br/>
	 *         100账号不存在<br/>
	 *         102_02 密码错误<br/>
	 *         102_03 支付密码为空
	 */
	public Result isExistsUserPayPassword(String userId, String plyPassword);
}