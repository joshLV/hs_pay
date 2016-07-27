package com.hszsd.user.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户信息<br/>
 * 贵州合石电子商务有限公司
 * 
 * @author 艾伍
 * @date 2016-04-25
 * @version 1.0.0
 * 
 */
public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户编号
	 */
	private String userId;

	/**
	 * 用户名称(唯一)
	 */
	private String username;

	/**
	 * 用户招商币
	 */
	private BigDecimal useCredit;
	/**
	 * 可用总额
	 */
	private BigDecimal useMoney;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * SEX 性别 1 男 2 女
	 */
	private int sex;

	/**
	 * VIP等级
	 */
	private String vipLevel;

	/**
	 * 民族
	 */
	private String nation;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机
	 */
	private String phone;
	/**
	 * 生日
	 */
	private String birthday;

	/**
	 * 地址
	 */
	private String address;
	/**
	 * 用户头像
	 */
	private String avatar;
	/**
	 * @return the 用户编号
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param 用户编号
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the 用户名称(唯一)
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param 用户名称
	 *            (唯一) the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the 用户积分
	 */
	public BigDecimal getUseCredit() {
		return useCredit;
	}

	/**
	 * @param 用户积分
	 *            the useCredit to set
	 */
	public void setUseCredit(BigDecimal useCredit) {
		this.useCredit = useCredit;
	}

	/**
	 * @return the 可用总额
	 */
	public BigDecimal getUseMoney() {
		return useMoney;
	}

	/**
	 * @param 可用总额
	 *            the useMoney to set
	 */
	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	/**
	 * @return the 真实姓名
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param 真实姓名
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the SEX性别1男2女
	 */
	public int getSex() {
		return sex;
	}

	/**
	 * @param SEX性别1男2女
	 *            the sex to set
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/**
	 * @return the VIP等级
	 */
	public String getVipLevel() {
		return vipLevel;
	}

	/**
	 * @param VIP等级
	 *            the vipLevel to set
	 */
	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	/**
	 * @return the 民族
	 */
	public String getNation() {
		return nation;
	}

	/**
	 * @param 民族
	 *            the nation to set
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}

	/**
	 * @return the 邮箱
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param 邮箱
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the 手机
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param 手机
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the 生日
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * @param 生日
	 *            the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the 地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param 地址
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "UserDTO{" +
				"address='" + address + '\'' +
				", userId='" + userId + '\'' +
				", username='" + username + '\'' +
				", useCredit=" + useCredit +
				", useMoney=" + useMoney +
				", realName='" + realName + '\'' +
				", sex=" + sex +
				", vipLevel='" + vipLevel + '\'' +
				", nation='" + nation + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", birthday='" + birthday + '\'' +
				", avatar='" + avatar + '\'' +
				'}';
	}
}
