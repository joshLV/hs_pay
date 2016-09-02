package com.hszsd.user.service;

import com.hszsd.common.util.Result;

import java.util.List;


/**
 * 用户信息接口<br/>
 * 贵州合石电子商务有限公司
 * 
 * @author yangwenjian
 * @since  2016-07-20
 * @version V1.0.0
 * 
 */
public interface UserService {


	/**
	 * 验证系统中用户名是否存在
	 * 
	 * @param username
	 *            用户名
	 * @return  为true表示存在<br/>
	 *         为false表示不存在
	 */
	public Result isExistsUserName( String username);

	/**
	 * 验证系统中手机号是否存在
	 * 
	 * @param phone
	 *            手机号
	 * @return  为true表示存在<br/>
	 *         为false表示不存在
	 */
	public Result isExistsPhone( String phone);

	/**
	 * 验证系统中邮箱是否存在
	 * 
	 * @param email
	 *            邮箱
	 * @return  为true表示存在<br/>
	 *         为false表示不存在
	 */
	public Result isExistsEmail(String email);


	/**
	 * 根据用户ID获取用户信息
	 * @param userId 用户ID
	 * @return 0000 执行成功<br/>
	 *         2000 参数为空<br/>
	 *         1000 系统错误<br/>
	 *         5005 账号不存在
	 */
	public Result getUserInfo(String userId);

	/**
	 * 根据用户名或者用户手机获取用户信息
	 * @param param 用户查询信息用户名或手机
	 * @return 0000 执行成功<br/>
	 *         2000 参数为空<br/>
	 *         1000 系统错误<br/>
	 *         5005 账号不存在
	 */
	public Result getUser(String param);

	/**
	 * 根据用户名，用户手机进行登录，
	 * @param userOrphone 用户名或手机
	 * @param password 用户登录密码
	 * @return 0000 执行成功<br/>
	 *         2000 参数缺失<br/>
	 *         5008 密码错误<br/>
	 *         5005 用户不存在<br/>
	 *         5000 账户冻结<br/>
	 *         5004 账户注销
	 */
	public Result getUser(String userOrphone,String password);



	/**
	 * 校验用户支付密码是否正确<br/>
	 * <b>传递加密过后数据进行处理</b>
	 * @param userId
	 *            用户编号
	 * @param payPassword
	 *            支付密码，为加密原始值
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
	public Result isExistsUserDESPPayPassword(String userId, String payPassword);


	/**
	 * 校验用户支付密码是否正确
	 *
	 * @param userId
	 *            用户编号
	 * @param payPassword
	 *            支付密码，为加密原始值
	 * @return  0000 执行成功<br/>
	 *          1000 操作失败<br/>
	 *          2000 参数缺失<br/>
	 *          5005 账号不存在<br/>
	 *          5009 支付密码错误<br/>
	 *          5010 未设置支付密码
	 */
	public Result isExistsUserPayPassword(String userId, String payPassword);


	/**
	 * 用户编号获取用户手机号和用户邮箱
	 * @param userIds 用户编号
	 * @return 0000 操作成功
	 *         2000 参数缺失
	 *         1000 操作失败
     */
	public Result getUserPhoneMail(List<String> userIds);


	/**
	 * 板块来源修改用户状态
	 * @param userId 用户ID
	 * @param blockSource 备注内容
	 * @return
     */
	public Result updateUserBlockSource(String userId,String blockSource);

	/**
	 * 手机号身份证用户信息反查
	 * @param phones 手机号集合
	 * @param cards 身份证号集合
     * @return
     */
	public Result counterUser(List<String> phones, List<String> cards);

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
	/*public Result getNameUser(String username, String password);*/

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
	/*public Result getPhoneUser(String phone, String password);*/

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
	 */
	/*public Result getNameUserInfo(String userName, String password);*/

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
	 */
/*	public Result getPhoneUserInfo(String phone, String password);*/

	/**
	 * 使用用户ID获取用户信息<br/>
	 * 当用户存在返回用户信息<br/>
	 * 当用户不存在时返回对应错误编号
	 *
	 * @param userId
	 *            用户编号
	 * @return 001成功<br/>
	 */
	/*public Result getIdUser(String userId);*/


	/**
	 * 使用用户名获取用户信息<br/>
	 * 当用户存在返回用户信息<br/>
	 * 当用户不存在时返回对应错误编号
	 * 
	 * @param username
	 *            用户编号
	 * @return 001成功<br/>
	 */
	/*public Result getNameUser(String username);*/



}