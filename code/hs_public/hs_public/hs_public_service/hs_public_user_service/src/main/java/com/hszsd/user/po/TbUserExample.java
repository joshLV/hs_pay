package com.hszsd.user.po;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TbUserExample {
	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public TbUserExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value,
				String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property
						+ " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1,
				Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property
						+ " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andUserIdIsNull() {
			addCriterion("u.USER_ID is null");
			return (Criteria) this;
		}

		public Criteria andUserIdIsNotNull() {
			addCriterion("u.USER_ID is not null");
			return (Criteria) this;
		}

		public Criteria andUserIdEqualTo(String value) {
			addCriterion("u.USER_ID =", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdNotEqualTo(String value) {
			addCriterion("u.USER_ID <>", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdGreaterThan(String value) {
			addCriterion("u.USER_ID >", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdGreaterThanOrEqualTo(String value) {
			addCriterion("u.USER_ID >=", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdLessThan(String value) {
			addCriterion("u.USER_ID <", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdLessThanOrEqualTo(String value) {
			addCriterion("u.USER_ID <=", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdLike(String value) {
			addCriterion("u.USER_ID like", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdNotLike(String value) {
			addCriterion("u.USER_ID not like", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdIn(List<String> values) {
			addCriterion("u.USER_ID in", values, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdNotIn(List<String> values) {
			addCriterion("u.USER_ID not in", values, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdBetween(String value1, String value2) {
			addCriterion("u.USER_ID between", value1, value2, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdNotBetween(String value1, String value2) {
			addCriterion("u.USER_ID not between", value1, value2, "userId");
			return (Criteria) this;
		}

		public Criteria andTypeIdIsNull() {
			addCriterion("TYPE_ID is null");
			return (Criteria) this;
		}

		public Criteria andTypeIdIsNotNull() {
			addCriterion("TYPE_ID is not null");
			return (Criteria) this;
		}

		public Criteria andTypeIdEqualTo(Integer value) {
			addCriterion("TYPE_ID =", value, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdNotEqualTo(Integer value) {
			addCriterion("TYPE_ID <>", value, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdGreaterThan(Integer value) {
			addCriterion("TYPE_ID >", value, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("TYPE_ID >=", value, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdLessThan(Integer value) {
			addCriterion("TYPE_ID <", value, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdLessThanOrEqualTo(Integer value) {
			addCriterion("TYPE_ID <=", value, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdIn(List<Integer> values) {
			addCriterion("TYPE_ID in", values, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdNotIn(List<Integer> values) {
			addCriterion("TYPE_ID not in", values, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdBetween(Integer value1, Integer value2) {
			addCriterion("TYPE_ID between", value1, value2, "typeId");
			return (Criteria) this;
		}

		public Criteria andTypeIdNotBetween(Integer value1, Integer value2) {
			addCriterion("TYPE_ID not between", value1, value2, "typeId");
			return (Criteria) this;
		}

		public Criteria andUsernameIsNull() {
			addCriterion("USERNAME is null");
			return (Criteria) this;
		}

		public Criteria andUsernameIsNotNull() {
			addCriterion("USERNAME is not null");
			return (Criteria) this;
		}

		public Criteria andUsernameEqualTo(String value) {
			addCriterion("USERNAME =", value, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameNotEqualTo(String value) {
			addCriterion("USERNAME <>", value, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameGreaterThan(String value) {
			addCriterion("USERNAME >", value, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameGreaterThanOrEqualTo(String value) {
			addCriterion("USERNAME >=", value, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameLessThan(String value) {
			addCriterion("USERNAME <", value, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameLessThanOrEqualTo(String value) {
			addCriterion("USERNAME <=", value, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameLike(String value) {
			addCriterion("USERNAME like", value, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameNotLike(String value) {
			addCriterion("USERNAME not like", value, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameIn(List<String> values) {
			addCriterion("USERNAME in", values, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameNotIn(List<String> values) {
			addCriterion("USERNAME not in", values, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameBetween(String value1, String value2) {
			addCriterion("USERNAME between", value1, value2, "username");
			return (Criteria) this;
		}

		public Criteria andUsernameNotBetween(String value1, String value2) {
			addCriterion("USERNAME not between", value1, value2, "username");
			return (Criteria) this;
		}

		public Criteria andPasswordIsNull() {
			addCriterion("PASSWORD is null");
			return (Criteria) this;
		}

		public Criteria andPasswordIsNotNull() {
			addCriterion("PASSWORD is not null");
			return (Criteria) this;
		}

		public Criteria andPasswordEqualTo(String value) {
			addCriterion("PASSWORD =", value, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordNotEqualTo(String value) {
			addCriterion("PASSWORD <>", value, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordGreaterThan(String value) {
			addCriterion("PASSWORD >", value, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordGreaterThanOrEqualTo(String value) {
			addCriterion("PASSWORD >=", value, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordLessThan(String value) {
			addCriterion("PASSWORD <", value, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordLessThanOrEqualTo(String value) {
			addCriterion("PASSWORD <=", value, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordLike(String value) {
			addCriterion("PASSWORD like", value, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordNotLike(String value) {
			addCriterion("PASSWORD not like", value, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordIn(List<String> values) {
			addCriterion("PASSWORD in", values, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordNotIn(List<String> values) {
			addCriterion("PASSWORD not in", values, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordBetween(String value1, String value2) {
			addCriterion("PASSWORD between", value1, value2, "password");
			return (Criteria) this;
		}

		public Criteria andPasswordNotBetween(String value1, String value2) {
			addCriterion("PASSWORD not between", value1, value2, "password");
			return (Criteria) this;
		}

		public Criteria andPaypasswordIsNull() {
			addCriterion("PAYPASSWORD is null");
			return (Criteria) this;
		}

		public Criteria andPaypasswordIsNotNull() {
			addCriterion("PAYPASSWORD is not null");
			return (Criteria) this;
		}

		public Criteria andPaypasswordEqualTo(String value) {
			addCriterion("PAYPASSWORD =", value, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordNotEqualTo(String value) {
			addCriterion("PAYPASSWORD <>", value, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordGreaterThan(String value) {
			addCriterion("PAYPASSWORD >", value, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordGreaterThanOrEqualTo(String value) {
			addCriterion("PAYPASSWORD >=", value, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordLessThan(String value) {
			addCriterion("PAYPASSWORD <", value, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordLessThanOrEqualTo(String value) {
			addCriterion("PAYPASSWORD <=", value, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordLike(String value) {
			addCriterion("PAYPASSWORD like", value, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordNotLike(String value) {
			addCriterion("PAYPASSWORD not like", value, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordIn(List<String> values) {
			addCriterion("PAYPASSWORD in", values, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordNotIn(List<String> values) {
			addCriterion("PAYPASSWORD not in", values, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordBetween(String value1, String value2) {
			addCriterion("PAYPASSWORD between", value1, value2, "paypassword");
			return (Criteria) this;
		}

		public Criteria andPaypasswordNotBetween(String value1, String value2) {
			addCriterion("PAYPASSWORD not between", value1, value2,
					"paypassword");
			return (Criteria) this;
		}

		public Criteria andIslockIsNull() {
			addCriterion("ISLOCK is null");
			return (Criteria) this;
		}

		public Criteria andIslockIsNotNull() {
			addCriterion("ISLOCK is not null");
			return (Criteria) this;
		}

		public Criteria andIslockEqualTo(Integer value) {
			addCriterion("ISLOCK =", value, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockNotEqualTo(Integer value) {
			addCriterion("ISLOCK <>", value, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockGreaterThan(Integer value) {
			addCriterion("ISLOCK >", value, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockGreaterThanOrEqualTo(Integer value) {
			addCriterion("ISLOCK >=", value, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockLessThan(Integer value) {
			addCriterion("ISLOCK <", value, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockLessThanOrEqualTo(Integer value) {
			addCriterion("ISLOCK <=", value, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockIn(List<Integer> values) {
			addCriterion("ISLOCK in", values, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockNotIn(List<Integer> values) {
			addCriterion("ISLOCK not in", values, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockBetween(Integer value1, Integer value2) {
			addCriterion("ISLOCK between", value1, value2, "islock");
			return (Criteria) this;
		}

		public Criteria andIslockNotBetween(Integer value1, Integer value2) {
			addCriterion("ISLOCK not between", value1, value2, "islock");
			return (Criteria) this;
		}

		public Criteria andStatusIsNull() {
			addCriterion("STATUS is null");
			return (Criteria) this;
		}

		public Criteria andStatusIsNotNull() {
			addCriterion("STATUS is not null");
			return (Criteria) this;
		}

		public Criteria andStatusEqualTo(Integer value) {
			addCriterion("STATUS =", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusNotEqualTo(Integer value) {
			addCriterion("STATUS <>", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusGreaterThan(Integer value) {
			addCriterion("STATUS >", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
			addCriterion("STATUS >=", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusLessThan(Integer value) {
			addCriterion("STATUS <", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusLessThanOrEqualTo(Integer value) {
			addCriterion("STATUS <=", value, "status");
			return (Criteria) this;
		}

		public Criteria andStatusIn(List<Integer> values) {
			addCriterion("STATUS in", values, "status");
			return (Criteria) this;
		}

		public Criteria andStatusNotIn(List<Integer> values) {
			addCriterion("STATUS not in", values, "status");
			return (Criteria) this;
		}

		public Criteria andStatusBetween(Integer value1, Integer value2) {
			addCriterion("STATUS between", value1, value2, "status");
			return (Criteria) this;
		}

		public Criteria andStatusNotBetween(Integer value1, Integer value2) {
			addCriterion("STATUS not between", value1, value2, "status");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueIsNull() {
			addCriterion("GROWTHVALUE is null");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueIsNotNull() {
			addCriterion("GROWTHVALUE is not null");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueEqualTo(BigDecimal value) {
			addCriterion("GROWTHVALUE =", value, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueNotEqualTo(BigDecimal value) {
			addCriterion("GROWTHVALUE <>", value, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueGreaterThan(BigDecimal value) {
			addCriterion("GROWTHVALUE >", value, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("GROWTHVALUE >=", value, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueLessThan(BigDecimal value) {
			addCriterion("GROWTHVALUE <", value, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueLessThanOrEqualTo(BigDecimal value) {
			addCriterion("GROWTHVALUE <=", value, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueIn(List<BigDecimal> values) {
			addCriterion("GROWTHVALUE in", values, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueNotIn(List<BigDecimal> values) {
			addCriterion("GROWTHVALUE not in", values, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueBetween(BigDecimal value1,
				BigDecimal value2) {
			addCriterion("GROWTHVALUE between", value1, value2, "growthvalue");
			return (Criteria) this;
		}

		public Criteria andGrowthvalueNotBetween(BigDecimal value1,
				BigDecimal value2) {
			addCriterion("GROWTHVALUE not between", value1, value2,
					"growthvalue");
			return (Criteria) this;
		}

		public Criteria andOldUsernameIsNull() {
			addCriterion("OLD_USERNAME is null");
			return (Criteria) this;
		}

		public Criteria andOldUsernameIsNotNull() {
			addCriterion("OLD_USERNAME is not null");
			return (Criteria) this;
		}

		public Criteria andOldUsernameEqualTo(String value) {
			addCriterion("OLD_USERNAME =", value, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameNotEqualTo(String value) {
			addCriterion("OLD_USERNAME <>", value, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameGreaterThan(String value) {
			addCriterion("OLD_USERNAME >", value, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameGreaterThanOrEqualTo(String value) {
			addCriterion("OLD_USERNAME >=", value, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameLessThan(String value) {
			addCriterion("OLD_USERNAME <", value, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameLessThanOrEqualTo(String value) {
			addCriterion("OLD_USERNAME <=", value, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameLike(String value) {
			addCriterion("OLD_USERNAME like", value, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameNotLike(String value) {
			addCriterion("OLD_USERNAME not like", value, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameIn(List<String> values) {
			addCriterion("OLD_USERNAME in", values, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameNotIn(List<String> values) {
			addCriterion("OLD_USERNAME not in", values, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameBetween(String value1, String value2) {
			addCriterion("OLD_USERNAME between", value1, value2, "oldUsername");
			return (Criteria) this;
		}

		public Criteria andOldUsernameNotBetween(String value1, String value2) {
			addCriterion("OLD_USERNAME not between", value1, value2,
					"oldUsername");
			return (Criteria) this;
		}

		public Criteria andRealNameIsNull() {
			addCriterion("REAL_NAME is null");
			return (Criteria) this;
		}

		public Criteria andRealNameIsNotNull() {
			addCriterion("REAL_NAME is not null");
			return (Criteria) this;
		}

		public Criteria andRealNameEqualTo(String value) {
			addCriterion("REAL_NAME =", value, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameNotEqualTo(String value) {
			addCriterion("REAL_NAME <>", value, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameGreaterThan(String value) {
			addCriterion("REAL_NAME >", value, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameGreaterThanOrEqualTo(String value) {
			addCriterion("REAL_NAME >=", value, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameLessThan(String value) {
			addCriterion("REAL_NAME <", value, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameLessThanOrEqualTo(String value) {
			addCriterion("REAL_NAME <=", value, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameLike(String value) {
			addCriterion("REAL_NAME like", value, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameNotLike(String value) {
			addCriterion("REAL_NAME not like", value, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameIn(List<String> values) {
			addCriterion("REAL_NAME in", values, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameNotIn(List<String> values) {
			addCriterion("REAL_NAME not in", values, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameBetween(String value1, String value2) {
			addCriterion("REAL_NAME between", value1, value2, "realName");
			return (Criteria) this;
		}

		public Criteria andRealNameNotBetween(String value1, String value2) {
			addCriterion("REAL_NAME not between", value1, value2, "realName");
			return (Criteria) this;
		}

		public Criteria andSexIsNull() {
			addCriterion("SEX is null");
			return (Criteria) this;
		}

		public Criteria andSexIsNotNull() {
			addCriterion("SEX is not null");
			return (Criteria) this;
		}

		public Criteria andSexEqualTo(Integer value) {
			addCriterion("SEX =", value, "sex");
			return (Criteria) this;
		}

		public Criteria andSexNotEqualTo(Integer value) {
			addCriterion("SEX <>", value, "sex");
			return (Criteria) this;
		}

		public Criteria andSexGreaterThan(Integer value) {
			addCriterion("SEX >", value, "sex");
			return (Criteria) this;
		}

		public Criteria andSexGreaterThanOrEqualTo(Integer value) {
			addCriterion("SEX >=", value, "sex");
			return (Criteria) this;
		}

		public Criteria andSexLessThan(Integer value) {
			addCriterion("SEX <", value, "sex");
			return (Criteria) this;
		}

		public Criteria andSexLessThanOrEqualTo(Integer value) {
			addCriterion("SEX <=", value, "sex");
			return (Criteria) this;
		}

		public Criteria andSexIn(List<Integer> values) {
			addCriterion("SEX in", values, "sex");
			return (Criteria) this;
		}

		public Criteria andSexNotIn(List<Integer> values) {
			addCriterion("SEX not in", values, "sex");
			return (Criteria) this;
		}

		public Criteria andSexBetween(Integer value1, Integer value2) {
			addCriterion("SEX between", value1, value2, "sex");
			return (Criteria) this;
		}

		public Criteria andSexNotBetween(Integer value1, Integer value2) {
			addCriterion("SEX not between", value1, value2, "sex");
			return (Criteria) this;
		}

		public Criteria andCardTypeIsNull() {
			addCriterion("CARD_TYPE is null");
			return (Criteria) this;
		}

		public Criteria andCardTypeIsNotNull() {
			addCriterion("CARD_TYPE is not null");
			return (Criteria) this;
		}

		public Criteria andCardTypeEqualTo(Integer value) {
			addCriterion("CARD_TYPE =", value, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeNotEqualTo(Integer value) {
			addCriterion("CARD_TYPE <>", value, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeGreaterThan(Integer value) {
			addCriterion("CARD_TYPE >", value, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeGreaterThanOrEqualTo(Integer value) {
			addCriterion("CARD_TYPE >=", value, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeLessThan(Integer value) {
			addCriterion("CARD_TYPE <", value, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeLessThanOrEqualTo(Integer value) {
			addCriterion("CARD_TYPE <=", value, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeIn(List<Integer> values) {
			addCriterion("CARD_TYPE in", values, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeNotIn(List<Integer> values) {
			addCriterion("CARD_TYPE not in", values, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeBetween(Integer value1, Integer value2) {
			addCriterion("CARD_TYPE between", value1, value2, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardTypeNotBetween(Integer value1, Integer value2) {
			addCriterion("CARD_TYPE not between", value1, value2, "cardType");
			return (Criteria) this;
		}

		public Criteria andCardIdIsNull() {
			addCriterion("CARD_ID is null");
			return (Criteria) this;
		}

		public Criteria andCardIdIsNotNull() {
			addCriterion("CARD_ID is not null");
			return (Criteria) this;
		}

		public Criteria andCardIdEqualTo(String value) {
			addCriterion("CARD_ID =", value, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdNotEqualTo(String value) {
			addCriterion("CARD_ID <>", value, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdGreaterThan(String value) {
			addCriterion("CARD_ID >", value, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdGreaterThanOrEqualTo(String value) {
			addCriterion("CARD_ID >=", value, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdLessThan(String value) {
			addCriterion("CARD_ID <", value, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdLessThanOrEqualTo(String value) {
			addCriterion("CARD_ID <=", value, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdLike(String value) {
			addCriterion("CARD_ID like", value, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdNotLike(String value) {
			addCriterion("CARD_ID not like", value, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdIn(List<String> values) {
			addCriterion("CARD_ID in", values, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdNotIn(List<String> values) {
			addCriterion("CARD_ID not in", values, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdBetween(String value1, String value2) {
			addCriterion("CARD_ID between", value1, value2, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardIdNotBetween(String value1, String value2) {
			addCriterion("CARD_ID not between", value1, value2, "cardId");
			return (Criteria) this;
		}

		public Criteria andCardPic1IsNull() {
			addCriterion("CARD_PIC1 is null");
			return (Criteria) this;
		}

		public Criteria andCardPic1IsNotNull() {
			addCriterion("CARD_PIC1 is not null");
			return (Criteria) this;
		}

		public Criteria andCardPic1EqualTo(String value) {
			addCriterion("CARD_PIC1 =", value, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1NotEqualTo(String value) {
			addCriterion("CARD_PIC1 <>", value, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1GreaterThan(String value) {
			addCriterion("CARD_PIC1 >", value, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1GreaterThanOrEqualTo(String value) {
			addCriterion("CARD_PIC1 >=", value, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1LessThan(String value) {
			addCriterion("CARD_PIC1 <", value, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1LessThanOrEqualTo(String value) {
			addCriterion("CARD_PIC1 <=", value, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1Like(String value) {
			addCriterion("CARD_PIC1 like", value, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1NotLike(String value) {
			addCriterion("CARD_PIC1 not like", value, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1In(List<String> values) {
			addCriterion("CARD_PIC1 in", values, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1NotIn(List<String> values) {
			addCriterion("CARD_PIC1 not in", values, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1Between(String value1, String value2) {
			addCriterion("CARD_PIC1 between", value1, value2, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic1NotBetween(String value1, String value2) {
			addCriterion("CARD_PIC1 not between", value1, value2, "cardPic1");
			return (Criteria) this;
		}

		public Criteria andCardPic2IsNull() {
			addCriterion("CARD_PIC2 is null");
			return (Criteria) this;
		}

		public Criteria andCardPic2IsNotNull() {
			addCriterion("CARD_PIC2 is not null");
			return (Criteria) this;
		}

		public Criteria andCardPic2EqualTo(String value) {
			addCriterion("CARD_PIC2 =", value, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2NotEqualTo(String value) {
			addCriterion("CARD_PIC2 <>", value, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2GreaterThan(String value) {
			addCriterion("CARD_PIC2 >", value, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2GreaterThanOrEqualTo(String value) {
			addCriterion("CARD_PIC2 >=", value, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2LessThan(String value) {
			addCriterion("CARD_PIC2 <", value, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2LessThanOrEqualTo(String value) {
			addCriterion("CARD_PIC2 <=", value, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2Like(String value) {
			addCriterion("CARD_PIC2 like", value, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2NotLike(String value) {
			addCriterion("CARD_PIC2 not like", value, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2In(List<String> values) {
			addCriterion("CARD_PIC2 in", values, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2NotIn(List<String> values) {
			addCriterion("CARD_PIC2 not in", values, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2Between(String value1, String value2) {
			addCriterion("CARD_PIC2 between", value1, value2, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andCardPic2NotBetween(String value1, String value2) {
			addCriterion("CARD_PIC2 not between", value1, value2, "cardPic2");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlIsNull() {
			addCriterion("LICPIC_URL is null");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlIsNotNull() {
			addCriterion("LICPIC_URL is not null");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlEqualTo(String value) {
			addCriterion("LICPIC_URL =", value, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlNotEqualTo(String value) {
			addCriterion("LICPIC_URL <>", value, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlGreaterThan(String value) {
			addCriterion("LICPIC_URL >", value, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlGreaterThanOrEqualTo(String value) {
			addCriterion("LICPIC_URL >=", value, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlLessThan(String value) {
			addCriterion("LICPIC_URL <", value, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlLessThanOrEqualTo(String value) {
			addCriterion("LICPIC_URL <=", value, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlLike(String value) {
			addCriterion("LICPIC_URL like", value, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlNotLike(String value) {
			addCriterion("LICPIC_URL not like", value, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlIn(List<String> values) {
			addCriterion("LICPIC_URL in", values, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlNotIn(List<String> values) {
			addCriterion("LICPIC_URL not in", values, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlBetween(String value1, String value2) {
			addCriterion("LICPIC_URL between", value1, value2, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andLicpicUrlNotBetween(String value1, String value2) {
			addCriterion("LICPIC_URL not between", value1, value2, "licpicUrl");
			return (Criteria) this;
		}

		public Criteria andUptimeIsNull() {
			addCriterion("UPTIME is null");
			return (Criteria) this;
		}

		public Criteria andUptimeIsNotNull() {
			addCriterion("UPTIME is not null");
			return (Criteria) this;
		}

		public Criteria andUptimeEqualTo(Long value) {
			addCriterion("UPTIME =", value, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeNotEqualTo(Long value) {
			addCriterion("UPTIME <>", value, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeGreaterThan(Long value) {
			addCriterion("UPTIME >", value, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeGreaterThanOrEqualTo(Long value) {
			addCriterion("UPTIME >=", value, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeLessThan(Long value) {
			addCriterion("UPTIME <", value, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeLessThanOrEqualTo(Long value) {
			addCriterion("UPTIME <=", value, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeIn(List<Long> values) {
			addCriterion("UPTIME in", values, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeNotIn(List<Long> values) {
			addCriterion("UPTIME not in", values, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeBetween(Long value1, Long value2) {
			addCriterion("UPTIME between", value1, value2, "uptime");
			return (Criteria) this;
		}

		public Criteria andUptimeNotBetween(Long value1, Long value2) {
			addCriterion("UPTIME not between", value1, value2, "uptime");
			return (Criteria) this;
		}

		public Criteria andAvatarIsNull() {
			addCriterion("AVATAR is null");
			return (Criteria) this;
		}

		public Criteria andAvatarIsNotNull() {
			addCriterion("AVATAR is not null");
			return (Criteria) this;
		}

		public Criteria andAvatarEqualTo(String value) {
			addCriterion("AVATAR =", value, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarNotEqualTo(String value) {
			addCriterion("AVATAR <>", value, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarGreaterThan(String value) {
			addCriterion("AVATAR >", value, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarGreaterThanOrEqualTo(String value) {
			addCriterion("AVATAR >=", value, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarLessThan(String value) {
			addCriterion("AVATAR <", value, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarLessThanOrEqualTo(String value) {
			addCriterion("AVATAR <=", value, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarLike(String value) {
			addCriterion("AVATAR like", value, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarNotLike(String value) {
			addCriterion("AVATAR not like", value, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarIn(List<String> values) {
			addCriterion("AVATAR in", values, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarNotIn(List<String> values) {
			addCriterion("AVATAR not in", values, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarBetween(String value1, String value2) {
			addCriterion("AVATAR between", value1, value2, "avatar");
			return (Criteria) this;
		}

		public Criteria andAvatarNotBetween(String value1, String value2) {
			addCriterion("AVATAR not between", value1, value2, "avatar");
			return (Criteria) this;
		}

		public Criteria andHeadUrlIsNull() {
			addCriterion("HEAD_URL is null");
			return (Criteria) this;
		}

		public Criteria andHeadUrlIsNotNull() {
			addCriterion("HEAD_URL is not null");
			return (Criteria) this;
		}

		public Criteria andHeadUrlEqualTo(String value) {
			addCriterion("HEAD_URL =", value, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlNotEqualTo(String value) {
			addCriterion("HEAD_URL <>", value, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlGreaterThan(String value) {
			addCriterion("HEAD_URL >", value, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlGreaterThanOrEqualTo(String value) {
			addCriterion("HEAD_URL >=", value, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlLessThan(String value) {
			addCriterion("HEAD_URL <", value, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlLessThanOrEqualTo(String value) {
			addCriterion("HEAD_URL <=", value, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlLike(String value) {
			addCriterion("HEAD_URL like", value, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlNotLike(String value) {
			addCriterion("HEAD_URL not like", value, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlIn(List<String> values) {
			addCriterion("HEAD_URL in", values, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlNotIn(List<String> values) {
			addCriterion("HEAD_URL not in", values, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlBetween(String value1, String value2) {
			addCriterion("HEAD_URL between", value1, value2, "headUrl");
			return (Criteria) this;
		}

		public Criteria andHeadUrlNotBetween(String value1, String value2) {
			addCriterion("HEAD_URL not between", value1, value2, "headUrl");
			return (Criteria) this;
		}

		public Criteria andNationIsNull() {
			addCriterion("NATION is null");
			return (Criteria) this;
		}

		public Criteria andNationIsNotNull() {
			addCriterion("NATION is not null");
			return (Criteria) this;
		}

		public Criteria andNationEqualTo(String value) {
			addCriterion("NATION =", value, "nation");
			return (Criteria) this;
		}

		public Criteria andNationNotEqualTo(String value) {
			addCriterion("NATION <>", value, "nation");
			return (Criteria) this;
		}

		public Criteria andNationGreaterThan(String value) {
			addCriterion("NATION >", value, "nation");
			return (Criteria) this;
		}

		public Criteria andNationGreaterThanOrEqualTo(String value) {
			addCriterion("NATION >=", value, "nation");
			return (Criteria) this;
		}

		public Criteria andNationLessThan(String value) {
			addCriterion("NATION <", value, "nation");
			return (Criteria) this;
		}

		public Criteria andNationLessThanOrEqualTo(String value) {
			addCriterion("NATION <=", value, "nation");
			return (Criteria) this;
		}

		public Criteria andNationLike(String value) {
			addCriterion("NATION like", value, "nation");
			return (Criteria) this;
		}

		public Criteria andNationNotLike(String value) {
			addCriterion("NATION not like", value, "nation");
			return (Criteria) this;
		}

		public Criteria andNationIn(List<String> values) {
			addCriterion("NATION in", values, "nation");
			return (Criteria) this;
		}

		public Criteria andNationNotIn(List<String> values) {
			addCriterion("NATION not in", values, "nation");
			return (Criteria) this;
		}

		public Criteria andNationBetween(String value1, String value2) {
			addCriterion("NATION between", value1, value2, "nation");
			return (Criteria) this;
		}

		public Criteria andNationNotBetween(String value1, String value2) {
			addCriterion("NATION not between", value1, value2, "nation");
			return (Criteria) this;
		}

		public Criteria andEmailIsNull() {
			addCriterion("EMAIL is null");
			return (Criteria) this;
		}

		public Criteria andEmailIsNotNull() {
			addCriterion("EMAIL is not null");
			return (Criteria) this;
		}

		public Criteria andEmailEqualTo(String value) {
			addCriterion("EMAIL =", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailNotEqualTo(String value) {
			addCriterion("EMAIL <>", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailGreaterThan(String value) {
			addCriterion("EMAIL >", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailGreaterThanOrEqualTo(String value) {
			addCriterion("EMAIL >=", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailLessThan(String value) {
			addCriterion("EMAIL <", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailLessThanOrEqualTo(String value) {
			addCriterion("EMAIL <=", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailLike(String value) {
			addCriterion("EMAIL like", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailNotLike(String value) {
			addCriterion("EMAIL not like", value, "email");
			return (Criteria) this;
		}

		public Criteria andEmailIn(List<String> values) {
			addCriterion("EMAIL in", values, "email");
			return (Criteria) this;
		}

		public Criteria andEmailNotIn(List<String> values) {
			addCriterion("EMAIL not in", values, "email");
			return (Criteria) this;
		}

		public Criteria andEmailBetween(String value1, String value2) {
			addCriterion("EMAIL between", value1, value2, "email");
			return (Criteria) this;
		}

		public Criteria andEmailNotBetween(String value1, String value2) {
			addCriterion("EMAIL not between", value1, value2, "email");
			return (Criteria) this;
		}

		public Criteria andPhoneIsNull() {
			addCriterion("PHONE is null");
			return (Criteria) this;
		}

		public Criteria andPhoneIsNotNull() {
			addCriterion("PHONE is not null");
			return (Criteria) this;
		}

		public Criteria andPhoneEqualTo(String value) {
			addCriterion("PHONE =", value, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneNotEqualTo(String value) {
			addCriterion("PHONE <>", value, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneGreaterThan(String value) {
			addCriterion("PHONE >", value, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneGreaterThanOrEqualTo(String value) {
			addCriterion("PHONE >=", value, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneLessThan(String value) {
			addCriterion("PHONE <", value, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneLessThanOrEqualTo(String value) {
			addCriterion("PHONE <=", value, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneLike(String value) {
			addCriterion("PHONE like", value, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneNotLike(String value) {
			addCriterion("PHONE not like", value, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneIn(List<String> values) {
			addCriterion("PHONE in", values, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneNotIn(List<String> values) {
			addCriterion("PHONE not in", values, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneBetween(String value1, String value2) {
			addCriterion("PHONE between", value1, value2, "phone");
			return (Criteria) this;
		}

		public Criteria andPhoneNotBetween(String value1, String value2) {
			addCriterion("PHONE not between", value1, value2, "phone");
			return (Criteria) this;
		}

		public Criteria andBirthdayIsNull() {
			addCriterion("BIRTHDAY is null");
			return (Criteria) this;
		}

		public Criteria andBirthdayIsNotNull() {
			addCriterion("BIRTHDAY is not null");
			return (Criteria) this;
		}

		public Criteria andBirthdayEqualTo(String value) {
			addCriterion("BIRTHDAY =", value, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayNotEqualTo(String value) {
			addCriterion("BIRTHDAY <>", value, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayGreaterThan(String value) {
			addCriterion("BIRTHDAY >", value, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayGreaterThanOrEqualTo(String value) {
			addCriterion("BIRTHDAY >=", value, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayLessThan(String value) {
			addCriterion("BIRTHDAY <", value, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayLessThanOrEqualTo(String value) {
			addCriterion("BIRTHDAY <=", value, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayLike(String value) {
			addCriterion("BIRTHDAY like", value, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayNotLike(String value) {
			addCriterion("BIRTHDAY not like", value, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayIn(List<String> values) {
			addCriterion("BIRTHDAY in", values, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayNotIn(List<String> values) {
			addCriterion("BIRTHDAY not in", values, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayBetween(String value1, String value2) {
			addCriterion("BIRTHDAY between", value1, value2, "birthday");
			return (Criteria) this;
		}

		public Criteria andBirthdayNotBetween(String value1, String value2) {
			addCriterion("BIRTHDAY not between", value1, value2, "birthday");
			return (Criteria) this;
		}

		public Criteria andRemindIsNull() {
			addCriterion("REMIND is null");
			return (Criteria) this;
		}

		public Criteria andRemindIsNotNull() {
			addCriterion("REMIND is not null");
			return (Criteria) this;
		}

		public Criteria andRemindEqualTo(Long value) {
			addCriterion("REMIND =", value, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindNotEqualTo(Long value) {
			addCriterion("REMIND <>", value, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindGreaterThan(Long value) {
			addCriterion("REMIND >", value, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindGreaterThanOrEqualTo(Long value) {
			addCriterion("REMIND >=", value, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindLessThan(Long value) {
			addCriterion("REMIND <", value, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindLessThanOrEqualTo(Long value) {
			addCriterion("REMIND <=", value, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindIn(List<Long> values) {
			addCriterion("REMIND in", values, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindNotIn(List<Long> values) {
			addCriterion("REMIND not in", values, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindBetween(Long value1, Long value2) {
			addCriterion("REMIND between", value1, value2, "remind");
			return (Criteria) this;
		}

		public Criteria andRemindNotBetween(Long value1, Long value2) {
			addCriterion("REMIND not between", value1, value2, "remind");
			return (Criteria) this;
		}

		public Criteria andAddressIsNull() {
			addCriterion("ADDRESS is null");
			return (Criteria) this;
		}

		public Criteria andAddressIsNotNull() {
			addCriterion("ADDRESS is not null");
			return (Criteria) this;
		}

		public Criteria andAddressEqualTo(String value) {
			addCriterion("ADDRESS =", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressNotEqualTo(String value) {
			addCriterion("ADDRESS <>", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressGreaterThan(String value) {
			addCriterion("ADDRESS >", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressGreaterThanOrEqualTo(String value) {
			addCriterion("ADDRESS >=", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressLessThan(String value) {
			addCriterion("ADDRESS <", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressLessThanOrEqualTo(String value) {
			addCriterion("ADDRESS <=", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressLike(String value) {
			addCriterion("ADDRESS like", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressNotLike(String value) {
			addCriterion("ADDRESS not like", value, "address");
			return (Criteria) this;
		}

		public Criteria andAddressIn(List<String> values) {
			addCriterion("ADDRESS in", values, "address");
			return (Criteria) this;
		}

		public Criteria andAddressNotIn(List<String> values) {
			addCriterion("ADDRESS not in", values, "address");
			return (Criteria) this;
		}

		public Criteria andAddressBetween(String value1, String value2) {
			addCriterion("ADDRESS between", value1, value2, "address");
			return (Criteria) this;
		}

		public Criteria andAddressNotBetween(String value1, String value2) {
			addCriterion("ADDRESS not between", value1, value2, "address");
			return (Criteria) this;
		}

		public Criteria andAddtimeIsNull() {
			addCriterion("ADDTIME is null");
			return (Criteria) this;
		}

		public Criteria andAddtimeIsNotNull() {
			addCriterion("ADDTIME is not null");
			return (Criteria) this;
		}

		public Criteria andAddtimeEqualTo(Long value) {
			addCriterion("ADDTIME =", value, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeNotEqualTo(Long value) {
			addCriterion("ADDTIME <>", value, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeGreaterThan(Long value) {
			addCriterion("ADDTIME >", value, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeGreaterThanOrEqualTo(Long value) {
			addCriterion("ADDTIME >=", value, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeLessThan(Long value) {
			addCriterion("ADDTIME <", value, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeLessThanOrEqualTo(Long value) {
			addCriterion("ADDTIME <=", value, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeIn(List<Long> values) {
			addCriterion("ADDTIME in", values, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeNotIn(List<Long> values) {
			addCriterion("ADDTIME not in", values, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeBetween(Long value1, Long value2) {
			addCriterion("ADDTIME between", value1, value2, "addtime");
			return (Criteria) this;
		}

		public Criteria andAddtimeNotBetween(Long value1, Long value2) {
			addCriterion("ADDTIME not between", value1, value2, "addtime");
			return (Criteria) this;
		}

		public Criteria andNidIsNull() {
			addCriterion("NID is null");
			return (Criteria) this;
		}

		public Criteria andNidIsNotNull() {
			addCriterion("NID is not null");
			return (Criteria) this;
		}

		public Criteria andNidEqualTo(String value) {
			addCriterion("NID =", value, "nid");
			return (Criteria) this;
		}

		public Criteria andNidNotEqualTo(String value) {
			addCriterion("NID <>", value, "nid");
			return (Criteria) this;
		}

		public Criteria andNidGreaterThan(String value) {
			addCriterion("NID >", value, "nid");
			return (Criteria) this;
		}

		public Criteria andNidGreaterThanOrEqualTo(String value) {
			addCriterion("NID >=", value, "nid");
			return (Criteria) this;
		}

		public Criteria andNidLessThan(String value) {
			addCriterion("NID <", value, "nid");
			return (Criteria) this;
		}

		public Criteria andNidLessThanOrEqualTo(String value) {
			addCriterion("NID <=", value, "nid");
			return (Criteria) this;
		}

		public Criteria andNidLike(String value) {
			addCriterion("NID like", value, "nid");
			return (Criteria) this;
		}

		public Criteria andNidNotLike(String value) {
			addCriterion("NID not like", value, "nid");
			return (Criteria) this;
		}

		public Criteria andNidIn(List<String> values) {
			addCriterion("NID in", values, "nid");
			return (Criteria) this;
		}

		public Criteria andNidNotIn(List<String> values) {
			addCriterion("NID not in", values, "nid");
			return (Criteria) this;
		}

		public Criteria andNidBetween(String value1, String value2) {
			addCriterion("NID between", value1, value2, "nid");
			return (Criteria) this;
		}

		public Criteria andNidNotBetween(String value1, String value2) {
			addCriterion("NID not between", value1, value2, "nid");
			return (Criteria) this;
		}
	}

	public static class Criteria extends GeneratedCriteria {

		protected Criteria() {
			super();
		}
	}

	public static class Criterion {
		private String condition;

		private Object value;

		private Object secondValue;

		private boolean noValue;

		private boolean singleValue;

		private boolean betweenValue;

		private boolean listValue;

		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue,
				String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}
}