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
public class UserInfo extends User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 1为投资用户 2为贷款用户
	 */
	private int typeId;
	/**
	 * 是否锁定(0开通 1锁定)
	 */
	private int islock;
	/**
	 * 状态(0关闭 1开通)
	 */
	private int status;
	/**
	 * 用户积分,关联vip等级
	 */
	private BigDecimal growthvalue;
	/**
	 * 实名认证状态 0：未认证；1-已认证
	 */
	private int realStatus;
	/**
	 * 邮箱认证状态 0：未认证；1-已认证
	 */
	private int emailStatus;
	/**
	 * 手机认证状态 0：未认证；1-已认证
	 */
	private int phoneStatus;
	/**
	 * 视频认证状态 0：未认证；1-已认证
	 */
	private int videoStatus;
	/**
	 * 现场认证状态 0：未认证；1-已认证
	 */
	private int sceneStatus;

	/**
	 * @return the 1为投资用户2为贷款用户
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param 1为投资用户2为贷款用户 the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	

	/**
	 * @return the 是否锁定(0开通1锁定)
	 */
	public int getIslock() {
		return islock;
	}

	/**
	 * @param 是否锁定(0开通1锁定) the islock to set
	 */
	public void setIslock(int islock) {
		this.islock = islock;
	}

	/**
	 * @return the 状态(0关闭1开通)
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param 状态
	 *            (0关闭1开通) the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the 用户积分关联vip等级
	 */
	public BigDecimal getGrowthvalue() {
		return growthvalue;
	}

	/**
	 * @param 用户积分关联vip等级
	 *            the growthvalue to set
	 */
	public void setGrowthvalue(BigDecimal growthvalue) {
		this.growthvalue = growthvalue;
	}

	/**
	 * @return the 实名认证状态0：未认证；1-已认证
	 */
	public int getRealStatus() {
		return realStatus;
	}

	/**
	 * @param 实名认证状态0
	 *            ：未认证；1-已认证 the realStatus to set
	 */
	public void setRealStatus(int realStatus) {
		this.realStatus = realStatus;
	}

	/**
	 * @return the 邮箱认证状态0：未认证；1-已认证
	 */
	public int getEmailStatus() {
		return emailStatus;
	}

	/**
	 * @param 邮箱认证状态0
	 *            ：未认证；1-已认证 the emailStatus to set
	 */
	public void setEmailStatus(int emailStatus) {
		this.emailStatus = emailStatus;
	}

	/**
	 * @return the 手机认证状态0：未认证；1-已认证
	 */
	public int getPhoneStatus() {
		return phoneStatus;
	}

	/**
	 * @param 手机认证状态0
	 *            ：未认证；1-已认证 the phoneStatus to set
	 */
	public void setPhoneStatus(int phoneStatus) {
		this.phoneStatus = phoneStatus;
	}

	/**
	 * @return the 视频认证状态0：未认证；1-已认证
	 */
	public int getVideoStatus() {
		return videoStatus;
	}

	/**
	 * @param 视频认证状态0
	 *            ：未认证；1-已认证 the videoStatus to set
	 */
	public void setVideoStatus(int videoStatus) {
		this.videoStatus = videoStatus;
	}

	/**
	 * @return the 现场认证状态0：未认证；1-已认证
	 */
	public int getSceneStatus() {
		return sceneStatus;
	}

	/**
	 * @param 现场认证状态0
	 *            ：未认证；1-已认证 the sceneStatus to set
	 */
	public void setSceneStatus(int sceneStatus) {
		this.sceneStatus = sceneStatus;
	}

}
