package org.sq.gameDemo.svr.game.characterEntity.model;

import java.util.ArrayList;
import java.util.List;

public class UserEntityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserEntityExample() {
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
     * @date 2019-08-06
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Byte value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Byte value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Byte value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Byte value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Byte value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Byte value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Byte> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Byte> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Byte value1, Byte value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Byte value1, Byte value2) {
            addCriterion("user_id not between", value1, value2, "userId");
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

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Byte value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Byte value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Byte value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Byte value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Byte value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Byte> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Byte> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Byte value1, Byte value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Byte value1, Byte value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andTypeIdIsNull() {
            addCriterion("type_id is null");
            return (Criteria) this;
        }

        public Criteria andTypeIdIsNotNull() {
            addCriterion("type_id is not null");
            return (Criteria) this;
        }

        public Criteria andTypeIdEqualTo(Byte value) {
            addCriterion("type_id =", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdNotEqualTo(Byte value) {
            addCriterion("type_id <>", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdGreaterThan(Byte value) {
            addCriterion("type_id >", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdGreaterThanOrEqualTo(Byte value) {
            addCriterion("type_id >=", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdLessThan(Byte value) {
            addCriterion("type_id <", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdLessThanOrEqualTo(Byte value) {
            addCriterion("type_id <=", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdIn(List<Byte> values) {
            addCriterion("type_id in", values, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdNotIn(List<Byte> values) {
            addCriterion("type_id not in", values, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdBetween(Byte value1, Byte value2) {
            addCriterion("type_id between", value1, value2, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdNotBetween(Byte value1, Byte value2) {
            addCriterion("type_id not between", value1, value2, "typeId");
            return (Criteria) this;
        }

        public Criteria andSenceIdIsNull() {
            addCriterion("sence_id is null");
            return (Criteria) this;
        }

        public Criteria andSenceIdIsNotNull() {
            addCriterion("sence_id is not null");
            return (Criteria) this;
        }

        public Criteria andSenceIdEqualTo(Byte value) {
            addCriterion("sence_id =", value, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdNotEqualTo(Byte value) {
            addCriterion("sence_id <>", value, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdGreaterThan(Byte value) {
            addCriterion("sence_id >", value, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdGreaterThanOrEqualTo(Byte value) {
            addCriterion("sence_id >=", value, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdLessThan(Byte value) {
            addCriterion("sence_id <", value, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdLessThanOrEqualTo(Byte value) {
            addCriterion("sence_id <=", value, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdIn(List<Byte> values) {
            addCriterion("sence_id in", values, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdNotIn(List<Byte> values) {
            addCriterion("sence_id not in", values, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdBetween(Byte value1, Byte value2) {
            addCriterion("sence_id between", value1, value2, "senceId");
            return (Criteria) this;
        }

        public Criteria andSenceIdNotBetween(Byte value1, Byte value2) {
            addCriterion("sence_id not between", value1, value2, "senceId");
            return (Criteria) this;
        }

        public Criteria andExpIsNull() {
            addCriterion("exp is null");
            return (Criteria) this;
        }

        public Criteria andExpIsNotNull() {
            addCriterion("exp is not null");
            return (Criteria) this;
        }

        public Criteria andExpEqualTo(Integer value) {
            addCriterion("exp =", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpNotEqualTo(Integer value) {
            addCriterion("exp <>", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpGreaterThan(Integer value) {
            addCriterion("exp >", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpGreaterThanOrEqualTo(Integer value) {
            addCriterion("exp >=", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpLessThan(Integer value) {
            addCriterion("exp <", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpLessThanOrEqualTo(Integer value) {
            addCriterion("exp <=", value, "exp");
            return (Criteria) this;
        }

        public Criteria andExpIn(List<Integer> values) {
            addCriterion("exp in", values, "exp");
            return (Criteria) this;
        }

        public Criteria andExpNotIn(List<Integer> values) {
            addCriterion("exp not in", values, "exp");
            return (Criteria) this;
        }

        public Criteria andExpBetween(Integer value1, Integer value2) {
            addCriterion("exp between", value1, value2, "exp");
            return (Criteria) this;
        }

        public Criteria andExpNotBetween(Integer value1, Integer value2) {
            addCriterion("exp not between", value1, value2, "exp");
            return (Criteria) this;
        }

        public Criteria andEquipmentsIsNull() {
            addCriterion("equipments is null");
            return (Criteria) this;
        }

        public Criteria andEquipmentsIsNotNull() {
            addCriterion("equipments is not null");
            return (Criteria) this;
        }

        public Criteria andEquipmentsEqualTo(String value) {
            addCriterion("equipments =", value, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsNotEqualTo(String value) {
            addCriterion("equipments <>", value, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsGreaterThan(String value) {
            addCriterion("equipments >", value, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsGreaterThanOrEqualTo(String value) {
            addCriterion("equipments >=", value, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsLessThan(String value) {
            addCriterion("equipments <", value, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsLessThanOrEqualTo(String value) {
            addCriterion("equipments <=", value, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsLike(String value) {
            addCriterion("equipments like", value, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsNotLike(String value) {
            addCriterion("equipments not like", value, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsIn(List<String> values) {
            addCriterion("equipments in", values, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsNotIn(List<String> values) {
            addCriterion("equipments not in", values, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsBetween(String value1, String value2) {
            addCriterion("equipments between", value1, value2, "equipments");
            return (Criteria) this;
        }

        public Criteria andEquipmentsNotBetween(String value1, String value2) {
            addCriterion("equipments not between", value1, value2, "equipments");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(Integer value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(Integer value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(Integer value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(Integer value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(Integer value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(Integer value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<Integer> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<Integer> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(Integer value1, Integer value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(Integer value1, Integer value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andGuildIdIsNull() {
            addCriterion("guild_id is null");
            return (Criteria) this;
        }

        public Criteria andGuildIdIsNotNull() {
            addCriterion("guild_id is not null");
            return (Criteria) this;
        }

        public Criteria andGuildIdEqualTo(Integer value) {
            addCriterion("guild_id =", value, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdNotEqualTo(Integer value) {
            addCriterion("guild_id <>", value, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdGreaterThan(Integer value) {
            addCriterion("guild_id >", value, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("guild_id >=", value, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdLessThan(Integer value) {
            addCriterion("guild_id <", value, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdLessThanOrEqualTo(Integer value) {
            addCriterion("guild_id <=", value, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdIn(List<Integer> values) {
            addCriterion("guild_id in", values, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdNotIn(List<Integer> values) {
            addCriterion("guild_id not in", values, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdBetween(Integer value1, Integer value2) {
            addCriterion("guild_id between", value1, value2, "guildId");
            return (Criteria) this;
        }

        public Criteria andGuildIdNotBetween(Integer value1, Integer value2) {
            addCriterion("guild_id not between", value1, value2, "guildId");
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
     * @date 2019-08-06
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