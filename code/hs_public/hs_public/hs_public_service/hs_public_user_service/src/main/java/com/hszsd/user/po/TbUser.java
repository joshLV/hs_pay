package com.hszsd.user.po;

import com.hszsd.entity.User;

public class TbUser extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5234014495104350933L;

	private String realName;

	private Integer sex;

	private Integer cardType;

	private String cardId;

	private String cardPic1;

	private String cardPic2;

	private String licpicUrl;

	private Long uptime;

	private String avatar;

	private String headUrl;

	private String nation;

	private String email;

	private String phone;

	private String birthday;

	private Long remind;

	private String address;

	private Long addtime;

	private String nid;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName == null ? null : realName.trim();
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId == null ? null : cardId.trim();
	}

	public String getCardPic1() {
		return cardPic1;
	}

	public void setCardPic1(String cardPic1) {
		this.cardPic1 = cardPic1 == null ? null : cardPic1.trim();
	}

	public String getCardPic2() {
		return cardPic2;
	}

	public void setCardPic2(String cardPic2) {
		this.cardPic2 = cardPic2 == null ? null : cardPic2.trim();
	}

	public String getLicpicUrl() {
		return licpicUrl;
	}

	public void setLicpicUrl(String licpicUrl) {
		this.licpicUrl = licpicUrl == null ? null : licpicUrl.trim();
	}

	public Long getUptime() {
		return uptime;
	}

	public void setUptime(Long uptime) {
		this.uptime = uptime;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar == null ? null : avatar.trim();
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl == null ? null : headUrl.trim();
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation == null ? null : nation.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday == null ? null : birthday.trim();
	}

	public Long getRemind() {
		return remind;
	}

	public void setRemind(Long remind) {
		this.remind = remind;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public Long getAddtime() {
		return addtime;
	}

	public void setAddtime(Long addtime) {
		this.addtime = addtime;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid == null ? null : nid.trim();
	}
}