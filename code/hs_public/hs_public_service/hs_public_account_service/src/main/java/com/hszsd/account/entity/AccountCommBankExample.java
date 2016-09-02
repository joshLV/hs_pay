package com.hszsd.account.entity;

import com.hszsd.entity.example.BaseExample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangWenJian on 2016-8-8.
 */
public class AccountCommBankExample extends BaseExample {
    protected String orderByClause;
    protected boolean distinct;
    protected List<AccountCommBankExample.Criteria> oredCriteria = new ArrayList();

    public AccountCommBankExample() {
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

    public List<AccountCommBankExample.Criteria> getOredCriteria() {
        return this.oredCriteria;
    }

    public void or(AccountCommBankExample.Criteria criteria) {
        this.oredCriteria.add(criteria);
    }

    public AccountCommBankExample.Criteria or() {
        AccountCommBankExample.Criteria criteria = this.createCriteriaInternal();
        this.oredCriteria.add(criteria);
        return criteria;
    }

    public AccountCommBankExample.Criteria createCriteria() {
        AccountCommBankExample.Criteria criteria = this.createCriteriaInternal();
        if(this.oredCriteria.size() == 0) {
            this.oredCriteria.add(criteria);
        }

        return criteria;
    }

    protected AccountCommBankExample.Criteria createCriteriaInternal() {
        AccountCommBankExample.Criteria criteria = new AccountCommBankExample.Criteria();
        return criteria;
    }

    public void clear() {
        this.oredCriteria.clear();
        this.orderByClause = null;
        this.distinct = false;
    }

    public static class Criteria extends AccountCommBankExample.GeneratedCriteria {
        protected Criteria() {
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

    protected abstract static class GeneratedCriteria {
        protected List<AccountCommBankExample.Criterion> criteria = new ArrayList();

        protected GeneratedCriteria() {
        }

        public boolean isValid() {
            return this.criteria.size() > 0;
        }

        public List<AccountCommBankExample.Criterion> getAllCriteria() {
            return this.criteria;
        }

        public List<AccountCommBankExample.Criterion> getCriteria() {
            return this.criteria;
        }

        protected void addCriterion(String condition) {
            if(condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            } else {
                this.criteria.add(new AccountCommBankExample.Criterion(condition));
            }
        }

        protected void addCriterion(String condition, Object value, String property) {
            if(value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            } else {
                this.criteria.add(new AccountCommBankExample.Criterion(condition, value));
            }
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if(value1 != null && value2 != null) {
                this.criteria.add(new AccountCommBankExample.Criterion(condition, value1, value2));
            } else {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
        }

        public AccountCommBankExample.Criteria andIdIsNull() {
            this.addCriterion("T.ID is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdIsNotNull() {
            this.addCriterion("T.ID is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdEqualTo(Long value) {
            this.addCriterion("T.ID =", value, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdNotEqualTo(Long value) {
            this.addCriterion("T.ID <>", value, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdGreaterThan(Long value) {
            this.addCriterion("T.ID >", value, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdGreaterThanOrEqualTo(Long value) {
            this.addCriterion("T.ID >=", value, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdLessThan(Long value) {
            this.addCriterion("T.ID <", value, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdLessThanOrEqualTo(Long value) {
            this.addCriterion("T.ID <=", value, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdIn(List<Long> values) {
            this.addCriterion("T.ID in", values, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdNotIn(List<Long> values) {
            this.addCriterion("T.ID not in", values, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdBetween(Long value1, Long value2) {
            this.addCriterion("T.ID between", value1, value2, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andIdNotBetween(Long value1, Long value2) {
            this.addCriterion("T.ID not between", value1, value2, "id");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdIsNull() {
            this.addCriterion("T.USER_ID is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdIsNotNull() {
            this.addCriterion("T.USER_ID is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdEqualTo(String value) {
            this.addCriterion("T.USER_ID =", value, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdNotEqualTo(String value) {
            this.addCriterion("T.USER_ID <>", value, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdGreaterThan(String value) {
            this.addCriterion("T.USER_ID >", value, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdGreaterThanOrEqualTo(String value) {
            this.addCriterion("T.USER_ID >=", value, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdLessThan(String value) {
            this.addCriterion("T.USER_ID <", value, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdLessThanOrEqualTo(String value) {
            this.addCriterion("T.USER_ID <=", value, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdLike(String value) {
            this.addCriterion("T.USER_ID like", value, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdNotLike(String value) {
            this.addCriterion("T.USER_ID not like", value, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdIn(List<String> values) {
            this.addCriterion("T.USER_ID in", values, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdNotIn(List<String> values) {
            this.addCriterion("T.USER_ID not in", values, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdBetween(String value1, String value2) {
            this.addCriterion("T.USER_ID between", value1, value2, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andUserIdNotBetween(String value1, String value2) {
            this.addCriterion("T.USER_ID not between", value1, value2, "userId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountIsNull() {
            this.addCriterion("T.ACCOUNT is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountIsNotNull() {
            this.addCriterion("T.ACCOUNT is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountEqualTo(String value) {
            this.addCriterion("T.ACCOUNT =", value, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountNotEqualTo(String value) {
            this.addCriterion("T.ACCOUNT <>", value, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountGreaterThan(String value) {
            this.addCriterion("T.ACCOUNT >", value, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountGreaterThanOrEqualTo(String value) {
            this.addCriterion("T.ACCOUNT >=", value, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountLessThan(String value) {
            this.addCriterion("T.ACCOUNT <", value, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountLessThanOrEqualTo(String value) {
            this.addCriterion("T.ACCOUNT <=", value, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountLike(String value) {
            this.addCriterion("T.ACCOUNT like", value, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountNotLike(String value) {
            this.addCriterion("T.ACCOUNT not like", value, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountIn(List<String> values) {
            this.addCriterion("T.ACCOUNT in", values, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountNotIn(List<String> values) {
            this.addCriterion("T.ACCOUNT not in", values, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountBetween(String value1, String value2) {
            this.addCriterion("T.ACCOUNT between", value1, value2, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAccountNotBetween(String value1, String value2) {
            this.addCriterion("T.ACCOUNT not between", value1, value2, "account");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankIsNull() {
            this.addCriterion("T.BRANK is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankIsNotNull() {
            this.addCriterion("T.BRANK is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankEqualTo(String value) {
            this.addCriterion("T.BRANK =", value, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankNotEqualTo(String value) {
            this.addCriterion("T.BRANK <>", value, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankGreaterThan(String value) {
            this.addCriterion("T.BRANK >", value, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankGreaterThanOrEqualTo(String value) {
            this.addCriterion("T.BRANK >=", value, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankLessThan(String value) {
            this.addCriterion("T.BRANK <", value, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankLessThanOrEqualTo(String value) {
            this.addCriterion("T.BRANK <=", value, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankLike(String value) {
            this.addCriterion("T.BRANK like", value, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankNotLike(String value) {
            this.addCriterion("T.BRANK not like", value, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankIn(List<String> values) {
            this.addCriterion("T.BRANK in", values, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankNotIn(List<String> values) {
            this.addCriterion("T.BRANK not in", values, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankBetween(String value1, String value2) {
            this.addCriterion("T.BRANK between", value1, value2, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBrankNotBetween(String value1, String value2) {
            this.addCriterion("T.BRANK not between", value1, value2, "brank");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchIsNull() {
            this.addCriterion("T.BRANCH is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchIsNotNull() {
            this.addCriterion("T.BRANCH is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchEqualTo(String value) {
            this.addCriterion("T.BRANCH =", value, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchNotEqualTo(String value) {
            this.addCriterion("T.BRANCH <>", value, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchGreaterThan(String value) {
            this.addCriterion("T.BRANCH >", value, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchGreaterThanOrEqualTo(String value) {
            this.addCriterion("T.BRANCH >=", value, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchLessThan(String value) {
            this.addCriterion("T.BRANCH <", value, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchLessThanOrEqualTo(String value) {
            this.addCriterion("T.BRANCH <=", value, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchLike(String value) {
            this.addCriterion("T.BRANCH like", value, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchNotLike(String value) {
            this.addCriterion("T.BRANCH not like", value, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchIn(List<String> values) {
            this.addCriterion("T.BRANCH in", values, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchNotIn(List<String> values) {
            this.addCriterion("T.BRANCH not in", values, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchBetween(String value1, String value2) {
            this.addCriterion("T.BRANCH between", value1, value2, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBranchNotBetween(String value1, String value2) {
            this.addCriterion("T.BRANCH not between", value1, value2, "branch");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceIsNull() {
            this.addCriterion("T.PROVINCE is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceIsNotNull() {
            this.addCriterion("T.PROVINCE is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceEqualTo(String value) {
            this.addCriterion("T.PROVINCE =", value, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceNotEqualTo(String value) {
            this.addCriterion("T.PROVINCE <>", value, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceGreaterThan(String value) {
            this.addCriterion("T.PROVINCE >", value, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceGreaterThanOrEqualTo(String value) {
            this.addCriterion("T.PROVINCE >=", value, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceLessThan(String value) {
            this.addCriterion("T.PROVINCE <", value, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceLessThanOrEqualTo(String value) {
            this.addCriterion("T.PROVINCE <=", value, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceLike(String value) {
            this.addCriterion("T.PROVINCE like", value, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceNotLike(String value) {
            this.addCriterion("T.PROVINCE not like", value, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceIn(List<String> values) {
            this.addCriterion("T.PROVINCE in", values, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceNotIn(List<String> values) {
            this.addCriterion("T.PROVINCE not in", values, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceBetween(String value1, String value2) {
            this.addCriterion("T.PROVINCE between", value1, value2, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andProvinceNotBetween(String value1, String value2) {
            this.addCriterion("T.PROVINCE not between", value1, value2, "province");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeIsNull() {
            this.addCriterion("T.ADDTIME is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeIsNotNull() {
            this.addCriterion("T.ADDTIME is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeEqualTo(Long value) {
            this.addCriterion("T.ADDTIME =", value, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeNotEqualTo(Long value) {
            this.addCriterion("T.ADDTIME <>", value, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeGreaterThan(Long value) {
            this.addCriterion("T.ADDTIME >", value, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeGreaterThanOrEqualTo(Long value) {
            this.addCriterion("T.ADDTIME >=", value, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeLessThan(Long value) {
            this.addCriterion("T.ADDTIME <", value, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeLessThanOrEqualTo(Long value) {
            this.addCriterion("T.ADDTIME <=", value, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeIn(List<Long> values) {
            this.addCriterion("T.ADDTIME in", values, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeNotIn(List<Long> values) {
            this.addCriterion("T.ADDTIME not in", values, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeBetween(Long value1, Long value2) {
            this.addCriterion("T.ADDTIME between", value1, value2, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAddtimeNotBetween(Long value1, Long value2) {
            this.addCriterion("T.ADDTIME not between", value1, value2, "addtime");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaIsNull() {
            this.addCriterion("T.AREA is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaIsNotNull() {
            this.addCriterion("T.AREA is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaEqualTo(String value) {
            this.addCriterion("T.AREA =", value, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaNotEqualTo(String value) {
            this.addCriterion("T.AREA <>", value, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaGreaterThan(String value) {
            this.addCriterion("T.AREA >", value, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaGreaterThanOrEqualTo(String value) {
            this.addCriterion("T.AREA >=", value, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaLessThan(String value) {
            this.addCriterion("T.AREA <", value, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaLessThanOrEqualTo(String value) {
            this.addCriterion("T.AREA <=", value, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaLike(String value) {
            this.addCriterion("T.AREA like", value, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaNotLike(String value) {
            this.addCriterion("T.AREA not like", value, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaIn(List<String> values) {
            this.addCriterion("T.AREA in", values, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaNotIn(List<String> values) {
            this.addCriterion("T.AREA not in", values, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaBetween(String value1, String value2) {
            this.addCriterion("T.AREA between", value1, value2, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andAreaNotBetween(String value1, String value2) {
            this.addCriterion("T.AREA not between", value1, value2, "area");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuIsNull() {
            this.addCriterion("T.QU is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuIsNotNull() {
            this.addCriterion("T.QU is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuEqualTo(String value) {
            this.addCriterion("T.QU =", value, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuNotEqualTo(String value) {
            this.addCriterion("T.QU <>", value, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuGreaterThan(String value) {
            this.addCriterion("T.QU >", value, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuGreaterThanOrEqualTo(String value) {
            this.addCriterion("T.QU >=", value, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuLessThan(String value) {
            this.addCriterion("T.QU <", value, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuLessThanOrEqualTo(String value) {
            this.addCriterion("T.QU <=", value, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuLike(String value) {
            this.addCriterion("T.QU like", value, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuNotLike(String value) {
            this.addCriterion("T.QU not like", value, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuIn(List<String> values) {
            this.addCriterion("T.QU in", values, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuNotIn(List<String> values) {
            this.addCriterion("T.QU not in", values, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuBetween(String value1, String value2) {
            this.addCriterion("T.QU between", value1, value2, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andQuNotBetween(String value1, String value2) {
            this.addCriterion("T.QU not between", value1, value2, "qu");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdIsNull() {
            this.addCriterion("T.REQUEST_ID is null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdIsNotNull() {
            this.addCriterion("T.REQUEST_ID is not null");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdEqualTo(String value) {
            this.addCriterion("T.REQUEST_ID =", value, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdNotEqualTo(String value) {
            this.addCriterion("T.REQUEST_ID <>", value, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdGreaterThan(String value) {
            this.addCriterion("T.REQUEST_ID >", value, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdGreaterThanOrEqualTo(String value) {
            this.addCriterion("T.REQUEST_ID >=", value, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdLessThan(String value) {
            this.addCriterion("T.REQUEST_ID <", value, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdLessThanOrEqualTo(String value) {
            this.addCriterion("T.REQUEST_ID <=", value, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdLike(String value) {
            this.addCriterion("T.REQUEST_ID like", value, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdNotLike(String value) {
            this.addCriterion("T.REQUEST_ID not like", value, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdIn(List<String> values) {
            this.addCriterion("T.REQUEST_ID in", values, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdNotIn(List<String> values) {
            this.addCriterion("T.REQUEST_ID not in", values, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdBetween(String value1, String value2) {
            this.addCriterion("T.REQUEST_ID between", value1, value2, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andRequestIdNotBetween(String value1, String value2) {
            this.addCriterion("T.REQUEST_ID not between", value1, value2, "requestId");
            return (AccountCommBankExample.Criteria)this;
        }


        public AccountCommBankExample.Criteria andImgUrlEqualTo(String value) {
            this.addCriterion("U.BANK_LOGO =", value, "imgUrl");
            return (AccountCommBankExample.Criteria)this;
        }

        public AccountCommBankExample.Criteria andBankCodeEqualTo(String value) {
            this.addCriterion("U.BANK_VALUE =", value, "bankCode");
            return (AccountCommBankExample.Criteria)this;
        }

    }
}