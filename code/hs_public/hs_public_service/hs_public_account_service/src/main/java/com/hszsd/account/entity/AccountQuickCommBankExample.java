package com.hszsd.account.entity;

import com.hszsd.entity.example.BaseExample;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangWenJian on 2016-8-9.
 */
public class AccountQuickCommBankExample extends BaseExample {

    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria = new ArrayList();

    public AccountQuickCommBankExample() {
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return this.distinct;
    }

    public List<AccountQuickCommBankExample.Criteria> getOredCriteria() {
        return this.oredCriteria;
    }

    public void or(AccountQuickCommBankExample.Criteria criteria) {
        this.oredCriteria.add(criteria);
    }

    public AccountQuickCommBankExample.Criteria or() {
        AccountQuickCommBankExample.Criteria criteria = this.createCriteriaInternal();
        this.oredCriteria.add(criteria);
        return criteria;
    }

    public AccountQuickCommBankExample.Criteria createCriteria() {
        AccountQuickCommBankExample.Criteria criteria = this.createCriteriaInternal();
        if(this.oredCriteria.size() == 0) {
            this.oredCriteria.add(criteria);
        }

        return criteria;
    }

    protected AccountQuickCommBankExample.Criteria createCriteriaInternal() {
        AccountQuickCommBankExample.Criteria criteria = new AccountQuickCommBankExample.Criteria();
        return criteria;
    }

    public void clear() {
        this.oredCriteria.clear();
        this.orderByClause = null;
        this.distinct = false;
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
            return this.condition;
        }

        public Object getValue() {
            return this.value;
        }

        public Object getSecondValue() {
            return this.secondValue;
        }

        public boolean isNoValue() {
            return this.noValue;
        }

        public boolean isSingleValue() {
            return this.singleValue;
        }

        public boolean isBetweenValue() {
            return this.betweenValue;
        }

        public boolean isListValue() {
            return this.listValue;
        }

        public String getTypeHandler() {
            return this.typeHandler;
        }

        protected Criterion(String condition) {
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if(value instanceof List) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }

        }

        protected Criterion(String condition, Object value) {
            this(condition, value, (String)null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, (String)null);
        }
    }

    public static class Criteria extends AccountQuickCommBankExample.GeneratedCriteria {
        protected Criteria() {
        }
    }

    protected abstract static class GeneratedCriteria {
        protected List<AccountQuickCommBankExample.Criterion> criteria = new ArrayList();

        protected GeneratedCriteria() {
        }

        public boolean isValid() {
            return this.criteria.size() > 0;
        }

        public List<AccountQuickCommBankExample.Criterion> getAllCriteria() {
            return this.criteria;
        }

        public List<AccountQuickCommBankExample.Criterion> getCriteria() {
            return this.criteria;
        }

        protected void addCriterion(String condition) {
            if(condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            } else {
                this.criteria.add(new AccountQuickCommBankExample.Criterion(condition));
            }
        }

        protected void addCriterion(String condition, Object value, String property) {
            if(value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            } else {
                this.criteria.add(new AccountQuickCommBankExample.Criterion(condition, value));
            }
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if(value1 != null && value2 != null) {
                this.criteria.add(new AccountQuickCommBankExample.Criterion(condition, value1, value2));
            } else {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
        }

        public AccountQuickCommBankExample.Criteria andIdIsNull() {
            this.addCriterion("Z.ID is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdIsNotNull() {
            this.addCriterion("Z.ID is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdEqualTo(String value) {
            this.addCriterion("Z.ID =", value, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdNotEqualTo(String value) {
            this.addCriterion("Z.ID <>", value, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdGreaterThan(String value) {
            this.addCriterion("Z.ID >", value, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.ID >=", value, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdLessThan(String value) {
            this.addCriterion("Z.ID <", value, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdLessThanOrEqualTo(String value) {
            this.addCriterion("Z.ID <=", value, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdLike(String value) {
            this.addCriterion("Z.ID like", value, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdNotLike(String value) {
            this.addCriterion("Z.ID not like", value, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdIn(List<String> values) {
            this.addCriterion("Z.ID in", values, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdNotIn(List<String> values) {
            this.addCriterion("Z.ID not in", values, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdBetween(String value1, String value2) {
            this.addCriterion("Z.ID between", value1, value2, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andIdNotBetween(String value1, String value2) {
            this.addCriterion("Z.ID not between", value1, value2, "id");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeIsNull() {
            this.addCriterion("Z.USER_TYPE is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeIsNotNull() {
            this.addCriterion("Z.USER_TYPE is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeEqualTo(Integer value) {
            this.addCriterion("Z.USER_TYPE =", value, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeNotEqualTo(Integer value) {
            this.addCriterion("Z.USER_TYPE <>", value, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeGreaterThan(Integer value) {
            this.addCriterion("Z.USER_TYPE >", value, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeGreaterThanOrEqualTo(Integer value) {
            this.addCriterion("Z.USER_TYPE >=", value, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeLessThan(Integer value) {
            this.addCriterion("Z.USER_TYPE <", value, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeLessThanOrEqualTo(Integer value) {
            this.addCriterion("Z.USER_TYPE <=", value, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeIn(List<Integer> values) {
            this.addCriterion("Z.USER_TYPE in", values, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeNotIn(List<Integer> values) {
            this.addCriterion("Z.USER_TYPE not in", values, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeBetween(Integer value1, Integer value2) {
            this.addCriterion("Z.USER_TYPE between", value1, value2, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserTypeNotBetween(Integer value1, Integer value2) {
            this.addCriterion("Z.USER_TYPE not between", value1, value2, "userType");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdIsNull() {
            this.addCriterion("Z.USER_ID is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdIsNotNull() {
            this.addCriterion("Z.USER_ID is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdEqualTo(String value) {
            this.addCriterion("Z.USER_ID =", value, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdNotEqualTo(String value) {
            this.addCriterion("Z.USER_ID <>", value, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdGreaterThan(String value) {
            this.addCriterion("Z.USER_ID >", value, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.USER_ID >=", value, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdLessThan(String value) {
            this.addCriterion("Z.USER_ID <", value, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdLessThanOrEqualTo(String value) {
            this.addCriterion("Z.USER_ID <=", value, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdLike(String value) {
            this.addCriterion("Z.USER_ID like", value, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdNotLike(String value) {
            this.addCriterion("Z.USER_ID not like", value, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdIn(List<String> values) {
            this.addCriterion("Z.USER_ID in", values, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdNotIn(List<String> values) {
            this.addCriterion("Z.USER_ID not in", values, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdBetween(String value1, String value2) {
            this.addCriterion("Z.USER_ID between", value1, value2, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIdNotBetween(String value1, String value2) {
            this.addCriterion("Z.USER_ID not between", value1, value2, "userId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdIsNull() {
            this.addCriterion("Z.REQUEST_ID is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdIsNotNull() {
            this.addCriterion("Z.REQUEST_ID is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdEqualTo(String value) {
            this.addCriterion("Z.REQUEST_ID =", value, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdNotEqualTo(String value) {
            this.addCriterion("Z.REQUEST_ID <>", value, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdGreaterThan(String value) {
            this.addCriterion("Z.REQUEST_ID >", value, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.REQUEST_ID >=", value, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdLessThan(String value) {
            this.addCriterion("Z.REQUEST_ID <", value, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdLessThanOrEqualTo(String value) {
            this.addCriterion("Z.REQUEST_ID <=", value, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdLike(String value) {
            this.addCriterion("Z.REQUEST_ID like", value, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdNotLike(String value) {
            this.addCriterion("Z.REQUEST_ID not like", value, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdIn(List<String> values) {
            this.addCriterion("Z.REQUEST_ID in", values, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdNotIn(List<String> values) {
            this.addCriterion("Z.REQUEST_ID not in", values, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdBetween(String value1, String value2) {
            this.addCriterion("Z.REQUEST_ID between", value1, value2, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andRequestIdNotBetween(String value1, String value2) {
            this.addCriterion("Z.REQUEST_ID not between", value1, value2, "requestId");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpIsNull() {
            this.addCriterion("Z.USER_IP is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpIsNotNull() {
            this.addCriterion("Z.USER_IP is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpEqualTo(String value) {
            this.addCriterion("Z.USER_IP =", value, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpNotEqualTo(String value) {
            this.addCriterion("Z.USER_IP <>", value, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpGreaterThan(String value) {
            this.addCriterion("Z.USER_IP >", value, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.USER_IP >=", value, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpLessThan(String value) {
            this.addCriterion("Z.USER_IP <", value, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpLessThanOrEqualTo(String value) {
            this.addCriterion("Z.USER_IP <=", value, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpLike(String value) {
            this.addCriterion("Z.USER_IP like", value, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpNotLike(String value) {
            this.addCriterion("Z.USER_IP not like", value, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpIn(List<String> values) {
            this.addCriterion("Z.USER_IP in", values, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpNotIn(List<String> values) {
            this.addCriterion("Z.USER_IP not in", values, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpBetween(String value1, String value2) {
            this.addCriterion("Z.USER_IP between", value1, value2, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andUserIpNotBetween(String value1, String value2) {
            this.addCriterion("Z.USER_IP not between", value1, value2, "userIp");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopIsNull() {
            this.addCriterion("Z.CARD_TOP is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopIsNotNull() {
            this.addCriterion("Z.CARD_TOP is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopEqualTo(String value) {
            this.addCriterion("Z.CARD_TOP =", value, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopNotEqualTo(String value) {
            this.addCriterion("Z.CARD_TOP <>", value, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopGreaterThan(String value) {
            this.addCriterion("Z.CARD_TOP >", value, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.CARD_TOP >=", value, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopLessThan(String value) {
            this.addCriterion("Z.CARD_TOP <", value, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopLessThanOrEqualTo(String value) {
            this.addCriterion("Z.CARD_TOP <=", value, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopLike(String value) {
            this.addCriterion("Z.CARD_TOP like", value, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopNotLike(String value) {
            this.addCriterion("Z.CARD_TOP not like", value, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopIn(List<String> values) {
            this.addCriterion("Z.CARD_TOP in", values, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopNotIn(List<String> values) {
            this.addCriterion("Z.CARD_TOP not in", values, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopBetween(String value1, String value2) {
            this.addCriterion("Z.CARD_TOP between", value1, value2, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardTopNotBetween(String value1, String value2) {
            this.addCriterion("Z.CARD_TOP not between", value1, value2, "cardTop");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastIsNull() {
            this.addCriterion("Z.CARD_LAST is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastIsNotNull() {
            this.addCriterion("Z.CARD_LAST is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastEqualTo(String value) {
            this.addCriterion("Z.CARD_LAST =", value, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastNotEqualTo(String value) {
            this.addCriterion("Z.CARD_LAST <>", value, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastGreaterThan(String value) {
            this.addCriterion("Z.CARD_LAST >", value, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.CARD_LAST >=", value, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastLessThan(String value) {
            this.addCriterion("Z.CARD_LAST <", value, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastLessThanOrEqualTo(String value) {
            this.addCriterion("Z.CARD_LAST <=", value, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastLike(String value) {
            this.addCriterion("Z.CARD_LAST like", value, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastNotLike(String value) {
            this.addCriterion("Z.CARD_LAST not like", value, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastIn(List<String> values) {
            this.addCriterion("Z.CARD_LAST in", values, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastNotIn(List<String> values) {
            this.addCriterion("Z.CARD_LAST not in", values, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastBetween(String value1, String value2) {
            this.addCriterion("Z.CARD_LAST between", value1, value2, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCardLastNotBetween(String value1, String value2) {
            this.addCriterion("Z.CARD_LAST not between", value1, value2, "cardLast");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeIsNull() {
            this.addCriterion("Z.BANK_CODE is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeIsNotNull() {
            this.addCriterion("Z.BANK_CODE is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeEqualTo(String value) {
            this.addCriterion("Z.BANK_CODE =", value, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeNotEqualTo(String value) {
            this.addCriterion("Z.BANK_CODE <>", value, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeGreaterThan(String value) {
            this.addCriterion("Z.BANK_CODE >", value, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.BANK_CODE >=", value, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeLessThan(String value) {
            this.addCriterion("Z.BANK_CODE <", value, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeLessThanOrEqualTo(String value) {
            this.addCriterion("Z.BANK_CODE <=", value, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeLike(String value) {
            this.addCriterion("Z.BANK_CODE like", value, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeNotLike(String value) {
            this.addCriterion("Z.BANK_CODE not like", value, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeIn(List<String> values) {
            this.addCriterion("Z.BANK_CODE in", values, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeNotIn(List<String> values) {
            this.addCriterion("Z.BANK_CODE not in", values, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeBetween(String value1, String value2) {
            this.addCriterion("Z.BANK_CODE between", value1, value2, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBankCodeNotBetween(String value1, String value2) {
            this.addCriterion("Z.BANK_CODE not between", value1, value2, "bankCode");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusIsNull() {
            this.addCriterion("Z.BIND_STATUS is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusIsNotNull() {
            this.addCriterion("Z.BIND_STATUS is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusEqualTo(Integer value) {
            this.addCriterion("Z.BIND_STATUS =", value, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusNotEqualTo(Integer value) {
            this.addCriterion("Z.BIND_STATUS <>", value, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusGreaterThan(Integer value) {
            this.addCriterion("Z.BIND_STATUS >", value, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusGreaterThanOrEqualTo(Integer value) {
            this.addCriterion("Z.BIND_STATUS >=", value, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusLessThan(Integer value) {
            this.addCriterion("Z.BIND_STATUS <", value, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusLessThanOrEqualTo(Integer value) {
            this.addCriterion("Z.BIND_STATUS <=", value, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusIn(List<Integer> values) {
            this.addCriterion("Z.BIND_STATUS in", values, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusNotIn(List<Integer> values) {
            this.addCriterion("Z.BIND_STATUS not in", values, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusBetween(Integer value1, Integer value2) {
            this.addCriterion("Z.BIND_STATUS between", value1, value2, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andBindStatusNotBetween(Integer value1, Integer value2) {
            this.addCriterion("Z.BIND_STATUS not between", value1, value2, "bindStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusIsNull() {
            this.addCriterion("Z.VALID_STATUS is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusIsNotNull() {
            this.addCriterion("Z.VALID_STATUS is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusEqualTo(Integer value) {
            this.addCriterion("Z.VALID_STATUS =", value, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusNotEqualTo(Integer value) {
            this.addCriterion("Z.VALID_STATUS <>", value, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusGreaterThan(Integer value) {
            this.addCriterion("Z.VALID_STATUS >", value, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusGreaterThanOrEqualTo(Integer value) {
            this.addCriterion("Z.VALID_STATUS >=", value, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusLessThan(Integer value) {
            this.addCriterion("Z.VALID_STATUS <", value, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusLessThanOrEqualTo(Integer value) {
            this.addCriterion("Z.VALID_STATUS <=", value, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusIn(List<Integer> values) {
            this.addCriterion("Z.VALID_STATUS in", values, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusNotIn(List<Integer> values) {
            this.addCriterion("Z.VALID_STATUS not in", values, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusBetween(Integer value1, Integer value2) {
            this.addCriterion("Z.VALID_STATUS between", value1, value2, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andValidStatusNotBetween(Integer value1, Integer value2) {
            this.addCriterion("Z.VALID_STATUS not between", value1, value2, "validStatus");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeIsNull() {
            this.addCriterion("Z.CREATE_TIME is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeIsNotNull() {
            this.addCriterion("Z.CREATE_TIME is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeEqualTo(Long value) {
            this.addCriterion("Z.CREATE_TIME =", value, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeNotEqualTo(Long value) {
            this.addCriterion("Z.CREATE_TIME <>", value, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeGreaterThan(Long value) {
            this.addCriterion("Z.CREATE_TIME >", value, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeGreaterThanOrEqualTo(Long value) {
            this.addCriterion("Z.CREATE_TIME >=", value, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeLessThan(Long value) {
            this.addCriterion("Z.CREATE_TIME <", value, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeLessThanOrEqualTo(Long value) {
            this.addCriterion("Z.CREATE_TIME <=", value, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeIn(List<Long> values) {
            this.addCriterion("Z.CREATE_TIME in", values, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeNotIn(List<Long> values) {
            this.addCriterion("Z.CREATE_TIME not in", values, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeBetween(Long value1, Long value2) {
            this.addCriterion("Z.CREATE_TIME between", value1, value2, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateTimeNotBetween(Long value1, Long value2) {
            this.addCriterion("Z.CREATE_TIME not between", value1, value2, "createTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByIsNull() {
            this.addCriterion("Z.CREATE_BY is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByIsNotNull() {
            this.addCriterion("Z.CREATE_BY is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByEqualTo(String value) {
            this.addCriterion("Z.CREATE_BY =", value, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByNotEqualTo(String value) {
            this.addCriterion("Z.CREATE_BY <>", value, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByGreaterThan(String value) {
            this.addCriterion("Z.CREATE_BY >", value, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.CREATE_BY >=", value, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByLessThan(String value) {
            this.addCriterion("Z.CREATE_BY <", value, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByLessThanOrEqualTo(String value) {
            this.addCriterion("Z.CREATE_BY <=", value, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByLike(String value) {
            this.addCriterion("Z.CREATE_BY like", value, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByNotLike(String value) {
            this.addCriterion("Z.CREATE_BY not like", value, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByIn(List<String> values) {
            this.addCriterion("Z.CREATE_BY in", values, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByNotIn(List<String> values) {
            this.addCriterion("Z.CREATE_BY not in", values, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByBetween(String value1, String value2) {
            this.addCriterion("Z.CREATE_BY between", value1, value2, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andCreateByNotBetween(String value1, String value2) {
            this.addCriterion("Z.CREATE_BY not between", value1, value2, "createBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeIsNull() {
            this.addCriterion("Z.MODIFY_TIME is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeIsNotNull() {
            this.addCriterion("Z.MODIFY_TIME is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeEqualTo(Long value) {
            this.addCriterion("Z.MODIFY_TIME =", value, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeNotEqualTo(Long value) {
            this.addCriterion("Z.MODIFY_TIME <>", value, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeGreaterThan(Long value) {
            this.addCriterion("Z.MODIFY_TIME >", value, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeGreaterThanOrEqualTo(Long value) {
            this.addCriterion("Z.MODIFY_TIME >=", value, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeLessThan(Long value) {
            this.addCriterion("Z.MODIFY_TIME <", value, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeLessThanOrEqualTo(Long value) {
            this.addCriterion("Z.MODIFY_TIME <=", value, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeIn(List<Long> values) {
            this.addCriterion("Z.MODIFY_TIME in", values, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeNotIn(List<Long> values) {
            this.addCriterion("Z.MODIFY_TIME not in", values, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeBetween(Long value1, Long value2) {
            this.addCriterion("Z.MODIFY_TIME between", value1, value2, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyTimeNotBetween(Long value1, Long value2) {
            this.addCriterion("Z.MODIFY_TIME not between", value1, value2, "modifyTime");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByIsNull() {
            this.addCriterion("Z.MODIFY_BY is null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByIsNotNull() {
            this.addCriterion("Z.MODIFY_BY is not null");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByEqualTo(String value) {
            this.addCriterion("Z.MODIFY_BY =", value, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByNotEqualTo(String value) {
            this.addCriterion("Z.MODIFY_BY <>", value, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByGreaterThan(String value) {
            this.addCriterion("Z.MODIFY_BY >", value, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByGreaterThanOrEqualTo(String value) {
            this.addCriterion("Z.MODIFY_BY >=", value, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByLessThan(String value) {
            this.addCriterion("Z.MODIFY_BY <", value, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByLessThanOrEqualTo(String value) {
            this.addCriterion("Z.MODIFY_BY <=", value, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByLike(String value) {
            this.addCriterion("Z.MODIFY_BY like", value, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByNotLike(String value) {
            this.addCriterion("Z.MODIFY_BY not like", value, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByIn(List<String> values) {
            this.addCriterion("Z.MODIFY_BY in", values, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByNotIn(List<String> values) {
            this.addCriterion("Z.MODIFY_BY not in", values, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByBetween(String value1, String value2) {
            this.addCriterion("Z.MODIFY_BY between", value1, value2, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andModifyByNotBetween(String value1, String value2) {
            this.addCriterion("Z.MODIFY_BY not between", value1, value2, "modifyBy");
            return (AccountQuickCommBankExample.Criteria)this;
        }

        public AccountQuickCommBankExample.Criteria andNameEqualTo(String value) {
            this.addCriterion("U.BANK_NAME =", value, "name");
            return (AccountQuickCommBankExample.Criteria)this;
        }

    }
}
