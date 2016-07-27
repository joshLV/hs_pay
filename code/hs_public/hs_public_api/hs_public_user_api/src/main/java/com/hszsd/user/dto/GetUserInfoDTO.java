package com.hszsd.user.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 获取用户基本信息实体
 */
public class GetUserInfoDTO implements Serializable {
    private static final long serialVersionUID = 6204616513020130871L;

    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 有效招商币
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
     *  1为投资用户 2为贷款用户
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
     * 实名认证状态0：未认证；1-已认证
     */
    private int realStatus;
    /**
     * 邮箱认证状态0：未认证；1-已认证
     */
    private int emailStatus;
    /**
     * 手机认证状态0：未认证；1-已认证
     */
    private int phoneStatus;
    /**
     * 视频认证状态0：未认证；1-已认证
     */
    private int videoStatus;
    /**
     * 现场认证状态0：未认证；1-已认证
     */
    private int sceneStatus;

    /**
     * 用户头像
     */
    private String avatar;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(int emailStatus) {
        this.emailStatus = emailStatus;
    }

    public BigDecimal getGrowthvalue() {
        return growthvalue;
    }

    public void setGrowthvalue(BigDecimal growthvalue) {
        this.growthvalue = growthvalue;
    }

    public int getIslock() {
        return islock;
    }

    public void setIslock(int islock) {
        this.islock = islock;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(int phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(int realStatus) {
        this.realStatus = realStatus;
    }

    public int getSceneStatus() {
        return sceneStatus;
    }

    public void setSceneStatus(int sceneStatus) {
        this.sceneStatus = sceneStatus;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getUseCredit() {
        return useCredit;
    }

    public void setUseCredit(BigDecimal useCredit) {
        this.useCredit = useCredit;
    }

    public BigDecimal getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(int videoStatus) {
        this.videoStatus = videoStatus;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "GetUserInfoDTO{" +
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
                ", typeId=" + typeId +
                ", islock=" + islock +
                ", status=" + status +
                ", growthvalue=" + growthvalue +
                ", realStatus=" + realStatus +
                ", emailStatus=" + emailStatus +
                ", phoneStatus=" + phoneStatus +
                ", videoStatus=" + videoStatus +
                ", sceneStatus=" + sceneStatus +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
