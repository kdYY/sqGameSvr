package org.sq.gameDemo.svr.game.guild.model;

import java.util.ArrayList;
import java.util.List;

public class GuildExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GuildExample() {
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

    /**
     * 
     * 
     * @author wcyong
     * 
     * @date 2019-08-30
     */
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

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(Integer value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(Integer value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(Integer value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(Integer value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(Integer value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<Integer> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<Integer> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(Integer value1, Integer value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("level not between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrIsNull() {
            addCriterion("warehouse_str is null");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrIsNotNull() {
            addCriterion("warehouse_str is not null");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrEqualTo(String value) {
            addCriterion("warehouse_str =", value, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrNotEqualTo(String value) {
            addCriterion("warehouse_str <>", value, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrGreaterThan(String value) {
            addCriterion("warehouse_str >", value, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrGreaterThanOrEqualTo(String value) {
            addCriterion("warehouse_str >=", value, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrLessThan(String value) {
            addCriterion("warehouse_str <", value, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrLessThanOrEqualTo(String value) {
            addCriterion("warehouse_str <=", value, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrLike(String value) {
            addCriterion("warehouse_str like", value, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrNotLike(String value) {
            addCriterion("warehouse_str not like", value, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrIn(List<String> values) {
            addCriterion("warehouse_str in", values, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrNotIn(List<String> values) {
            addCriterion("warehouse_str not in", values, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrBetween(String value1, String value2) {
            addCriterion("warehouse_str between", value1, value2, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseStrNotBetween(String value1, String value2) {
            addCriterion("warehouse_str not between", value1, value2, "warehouseStr");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeIsNull() {
            addCriterion("warehouse_size is null");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeIsNotNull() {
            addCriterion("warehouse_size is not null");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeEqualTo(Integer value) {
            addCriterion("warehouse_size =", value, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeNotEqualTo(Integer value) {
            addCriterion("warehouse_size <>", value, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeGreaterThan(Integer value) {
            addCriterion("warehouse_size >", value, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("warehouse_size >=", value, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeLessThan(Integer value) {
            addCriterion("warehouse_size <", value, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeLessThanOrEqualTo(Integer value) {
            addCriterion("warehouse_size <=", value, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeIn(List<Integer> values) {
            addCriterion("warehouse_size in", values, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeNotIn(List<Integer> values) {
            addCriterion("warehouse_size not in", values, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeBetween(Integer value1, Integer value2) {
            addCriterion("warehouse_size between", value1, value2, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andWarehouseSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("warehouse_size not between", value1, value2, "warehouseSize");
            return (Criteria) this;
        }

        public Criteria andDonateStrIsNull() {
            addCriterion("donate_str is null");
            return (Criteria) this;
        }

        public Criteria andDonateStrIsNotNull() {
            addCriterion("donate_str is not null");
            return (Criteria) this;
        }

        public Criteria andDonateStrEqualTo(String value) {
            addCriterion("donate_str =", value, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrNotEqualTo(String value) {
            addCriterion("donate_str <>", value, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrGreaterThan(String value) {
            addCriterion("donate_str >", value, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrGreaterThanOrEqualTo(String value) {
            addCriterion("donate_str >=", value, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrLessThan(String value) {
            addCriterion("donate_str <", value, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrLessThanOrEqualTo(String value) {
            addCriterion("donate_str <=", value, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrLike(String value) {
            addCriterion("donate_str like", value, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrNotLike(String value) {
            addCriterion("donate_str not like", value, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrIn(List<String> values) {
            addCriterion("donate_str in", values, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrNotIn(List<String> values) {
            addCriterion("donate_str not in", values, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrBetween(String value1, String value2) {
            addCriterion("donate_str between", value1, value2, "donateStr");
            return (Criteria) this;
        }

        public Criteria andDonateStrNotBetween(String value1, String value2) {
            addCriterion("donate_str not between", value1, value2, "donateStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrIsNull() {
            addCriterion("join_request_str is null");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrIsNotNull() {
            addCriterion("join_request_str is not null");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrEqualTo(String value) {
            addCriterion("join_request_str =", value, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrNotEqualTo(String value) {
            addCriterion("join_request_str <>", value, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrGreaterThan(String value) {
            addCriterion("join_request_str >", value, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrGreaterThanOrEqualTo(String value) {
            addCriterion("join_request_str >=", value, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrLessThan(String value) {
            addCriterion("join_request_str <", value, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrLessThanOrEqualTo(String value) {
            addCriterion("join_request_str <=", value, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrLike(String value) {
            addCriterion("join_request_str like", value, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrNotLike(String value) {
            addCriterion("join_request_str not like", value, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrIn(List<String> values) {
            addCriterion("join_request_str in", values, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrNotIn(List<String> values) {
            addCriterion("join_request_str not in", values, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrBetween(String value1, String value2) {
            addCriterion("join_request_str between", value1, value2, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andJoinRequestStrNotBetween(String value1, String value2) {
            addCriterion("join_request_str not between", value1, value2, "joinRequestStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrIsNull() {
            addCriterion("member_str is null");
            return (Criteria) this;
        }

        public Criteria andMemberStrIsNotNull() {
            addCriterion("member_str is not null");
            return (Criteria) this;
        }

        public Criteria andMemberStrEqualTo(String value) {
            addCriterion("member_str =", value, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrNotEqualTo(String value) {
            addCriterion("member_str <>", value, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrGreaterThan(String value) {
            addCriterion("member_str >", value, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrGreaterThanOrEqualTo(String value) {
            addCriterion("member_str >=", value, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrLessThan(String value) {
            addCriterion("member_str <", value, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrLessThanOrEqualTo(String value) {
            addCriterion("member_str <=", value, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrLike(String value) {
            addCriterion("member_str like", value, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrNotLike(String value) {
            addCriterion("member_str not like", value, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrIn(List<String> values) {
            addCriterion("member_str in", values, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrNotIn(List<String> values) {
            addCriterion("member_str not in", values, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrBetween(String value1, String value2) {
            addCriterion("member_str between", value1, value2, "memberStr");
            return (Criteria) this;
        }

        public Criteria andMemberStrNotBetween(String value1, String value2) {
            addCriterion("member_str not between", value1, value2, "memberStr");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * 
     * 
     * @author wcyong
     * 
     * @date 2019-08-30
     */
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

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
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