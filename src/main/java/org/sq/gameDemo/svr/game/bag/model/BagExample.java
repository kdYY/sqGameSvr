package org.sq.gameDemo.svr.game.bag.model;

import java.util.ArrayList;
import java.util.List;

public class BagExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BagExample() {
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
     * @date 2019-08-12
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

        public Criteria andUnIdIsNull() {
            addCriterion("un_id is null");
            return (Criteria) this;
        }

        public Criteria andUnIdIsNotNull() {
            addCriterion("un_id is not null");
            return (Criteria) this;
        }

        public Criteria andUnIdEqualTo(Integer value) {
            addCriterion("un_id =", value, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdNotEqualTo(Integer value) {
            addCriterion("un_id <>", value, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdGreaterThan(Integer value) {
            addCriterion("un_id >", value, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("un_id >=", value, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdLessThan(Integer value) {
            addCriterion("un_id <", value, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdLessThanOrEqualTo(Integer value) {
            addCriterion("un_id <=", value, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdIn(List<Integer> values) {
            addCriterion("un_id in", values, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdNotIn(List<Integer> values) {
            addCriterion("un_id not in", values, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdBetween(Integer value1, Integer value2) {
            addCriterion("un_id between", value1, value2, "unId");
            return (Criteria) this;
        }

        public Criteria andUnIdNotBetween(Integer value1, Integer value2) {
            addCriterion("un_id not between", value1, value2, "unId");
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

        public Criteria andSizeIsNull() {
            addCriterion("size is null");
            return (Criteria) this;
        }

        public Criteria andSizeIsNotNull() {
            addCriterion("size is not null");
            return (Criteria) this;
        }

        public Criteria andSizeEqualTo(Integer value) {
            addCriterion("size =", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotEqualTo(Integer value) {
            addCriterion("size <>", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThan(Integer value) {
            addCriterion("size >", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("size >=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThan(Integer value) {
            addCriterion("size <", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThanOrEqualTo(Integer value) {
            addCriterion("size <=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeIn(List<Integer> values) {
            addCriterion("size in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotIn(List<Integer> values) {
            addCriterion("size not in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeBetween(Integer value1, Integer value2) {
            addCriterion("size between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("size not between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andItemStrIsNull() {
            addCriterion("item_str is null");
            return (Criteria) this;
        }

        public Criteria andItemStrIsNotNull() {
            addCriterion("item_str is not null");
            return (Criteria) this;
        }

        public Criteria andItemStrEqualTo(String value) {
            addCriterion("item_str =", value, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrNotEqualTo(String value) {
            addCriterion("item_str <>", value, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrGreaterThan(String value) {
            addCriterion("item_str >", value, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrGreaterThanOrEqualTo(String value) {
            addCriterion("item_str >=", value, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrLessThan(String value) {
            addCriterion("item_str <", value, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrLessThanOrEqualTo(String value) {
            addCriterion("item_str <=", value, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrLike(String value) {
            addCriterion("item_str like", value, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrNotLike(String value) {
            addCriterion("item_str not like", value, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrIn(List<String> values) {
            addCriterion("item_str in", values, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrNotIn(List<String> values) {
            addCriterion("item_str not in", values, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrBetween(String value1, String value2) {
            addCriterion("item_str between", value1, value2, "itemStr");
            return (Criteria) this;
        }

        public Criteria andItemStrNotBetween(String value1, String value2) {
            addCriterion("item_str not between", value1, value2, "itemStr");
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
     * @date 2019-08-12
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