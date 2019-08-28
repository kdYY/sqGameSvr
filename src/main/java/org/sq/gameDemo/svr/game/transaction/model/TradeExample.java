package org.sq.gameDemo.svr.game.transaction.model;

import java.util.ArrayList;
import java.util.List;

public class TradeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TradeExample() {
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
     * @date 2019-08-28
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

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Long value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Long value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Long value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Long value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Long value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Long> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Long> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Long value1, Long value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Long value1, Long value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeIsNull() {
            addCriterion("keep_time is null");
            return (Criteria) this;
        }

        public Criteria andKeepTimeIsNotNull() {
            addCriterion("keep_time is not null");
            return (Criteria) this;
        }

        public Criteria andKeepTimeEqualTo(Long value) {
            addCriterion("keep_time =", value, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeNotEqualTo(Long value) {
            addCriterion("keep_time <>", value, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeGreaterThan(Long value) {
            addCriterion("keep_time >", value, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("keep_time >=", value, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeLessThan(Long value) {
            addCriterion("keep_time <", value, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeLessThanOrEqualTo(Long value) {
            addCriterion("keep_time <=", value, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeIn(List<Long> values) {
            addCriterion("keep_time in", values, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeNotIn(List<Long> values) {
            addCriterion("keep_time not in", values, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeBetween(Long value1, Long value2) {
            addCriterion("keep_time between", value1, value2, "keepTime");
            return (Criteria) this;
        }

        public Criteria andKeepTimeNotBetween(Long value1, Long value2) {
            addCriterion("keep_time not between", value1, value2, "keepTime");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdIsNull() {
            addCriterion("owner_un_id is null");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdIsNotNull() {
            addCriterion("owner_un_id is not null");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdEqualTo(Integer value) {
            addCriterion("owner_un_id =", value, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdNotEqualTo(Integer value) {
            addCriterion("owner_un_id <>", value, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdGreaterThan(Integer value) {
            addCriterion("owner_un_id >", value, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("owner_un_id >=", value, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdLessThan(Integer value) {
            addCriterion("owner_un_id <", value, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdLessThanOrEqualTo(Integer value) {
            addCriterion("owner_un_id <=", value, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdIn(List<Integer> values) {
            addCriterion("owner_un_id in", values, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdNotIn(List<Integer> values) {
            addCriterion("owner_un_id not in", values, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdBetween(Integer value1, Integer value2) {
            addCriterion("owner_un_id between", value1, value2, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andOwnerUnIdNotBetween(Integer value1, Integer value2) {
            addCriterion("owner_un_id not between", value1, value2, "ownerUnId");
            return (Criteria) this;
        }

        public Criteria andTradeModelIsNull() {
            addCriterion("trade_model is null");
            return (Criteria) this;
        }

        public Criteria andTradeModelIsNotNull() {
            addCriterion("trade_model is not null");
            return (Criteria) this;
        }

        public Criteria andTradeModelEqualTo(Integer value) {
            addCriterion("trade_model =", value, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelNotEqualTo(Integer value) {
            addCriterion("trade_model <>", value, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelGreaterThan(Integer value) {
            addCriterion("trade_model >", value, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelGreaterThanOrEqualTo(Integer value) {
            addCriterion("trade_model >=", value, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelLessThan(Integer value) {
            addCriterion("trade_model <", value, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelLessThanOrEqualTo(Integer value) {
            addCriterion("trade_model <=", value, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelIn(List<Integer> values) {
            addCriterion("trade_model in", values, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelNotIn(List<Integer> values) {
            addCriterion("trade_model not in", values, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelBetween(Integer value1, Integer value2) {
            addCriterion("trade_model between", value1, value2, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andTradeModelNotBetween(Integer value1, Integer value2) {
            addCriterion("trade_model not between", value1, value2, "tradeModel");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrIsNull() {
            addCriterion("items_map_str is null");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrIsNotNull() {
            addCriterion("items_map_str is not null");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrEqualTo(String value) {
            addCriterion("items_map_str =", value, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrNotEqualTo(String value) {
            addCriterion("items_map_str <>", value, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrGreaterThan(String value) {
            addCriterion("items_map_str >", value, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrGreaterThanOrEqualTo(String value) {
            addCriterion("items_map_str >=", value, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrLessThan(String value) {
            addCriterion("items_map_str <", value, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrLessThanOrEqualTo(String value) {
            addCriterion("items_map_str <=", value, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrLike(String value) {
            addCriterion("items_map_str like", value, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrNotLike(String value) {
            addCriterion("items_map_str not like", value, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrIn(List<String> values) {
            addCriterion("items_map_str in", values, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrNotIn(List<String> values) {
            addCriterion("items_map_str not in", values, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrBetween(String value1, String value2) {
            addCriterion("items_map_str between", value1, value2, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemsMapStrNotBetween(String value1, String value2) {
            addCriterion("items_map_str not between", value1, value2, "itemsMapStr");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdIsNull() {
            addCriterion("item_info_id is null");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdIsNotNull() {
            addCriterion("item_info_id is not null");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdEqualTo(Integer value) {
            addCriterion("item_info_id =", value, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdNotEqualTo(Integer value) {
            addCriterion("item_info_id <>", value, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdGreaterThan(Integer value) {
            addCriterion("item_info_id >", value, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("item_info_id >=", value, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdLessThan(Integer value) {
            addCriterion("item_info_id <", value, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdLessThanOrEqualTo(Integer value) {
            addCriterion("item_info_id <=", value, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdIn(List<Integer> values) {
            addCriterion("item_info_id in", values, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdNotIn(List<Integer> values) {
            addCriterion("item_info_id not in", values, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdBetween(Integer value1, Integer value2) {
            addCriterion("item_info_id between", value1, value2, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andItemInfoIdNotBetween(Integer value1, Integer value2) {
            addCriterion("item_info_id not between", value1, value2, "itemInfoId");
            return (Criteria) this;
        }

        public Criteria andCountIsNull() {
            addCriterion("count is null");
            return (Criteria) this;
        }

        public Criteria andCountIsNotNull() {
            addCriterion("count is not null");
            return (Criteria) this;
        }

        public Criteria andCountEqualTo(Integer value) {
            addCriterion("count =", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountNotEqualTo(Integer value) {
            addCriterion("count <>", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountGreaterThan(Integer value) {
            addCriterion("count >", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("count >=", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountLessThan(Integer value) {
            addCriterion("count <", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountLessThanOrEqualTo(Integer value) {
            addCriterion("count <=", value, "count");
            return (Criteria) this;
        }

        public Criteria andCountIn(List<Integer> values) {
            addCriterion("count in", values, "count");
            return (Criteria) this;
        }

        public Criteria andCountNotIn(List<Integer> values) {
            addCriterion("count not in", values, "count");
            return (Criteria) this;
        }

        public Criteria andCountBetween(Integer value1, Integer value2) {
            addCriterion("count between", value1, value2, "count");
            return (Criteria) this;
        }

        public Criteria andCountNotBetween(Integer value1, Integer value2) {
            addCriterion("count not between", value1, value2, "count");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(Integer value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(Integer value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(Integer value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(Integer value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(Integer value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(Integer value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<Integer> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<Integer> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(Integer value1, Integer value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(Integer value1, Integer value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andFinishIsNull() {
            addCriterion("finish is null");
            return (Criteria) this;
        }

        public Criteria andFinishIsNotNull() {
            addCriterion("finish is not null");
            return (Criteria) this;
        }

        public Criteria andFinishEqualTo(Boolean value) {
            addCriterion("finish =", value, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishNotEqualTo(Boolean value) {
            addCriterion("finish <>", value, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishGreaterThan(Boolean value) {
            addCriterion("finish >", value, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishGreaterThanOrEqualTo(Boolean value) {
            addCriterion("finish >=", value, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishLessThan(Boolean value) {
            addCriterion("finish <", value, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishLessThanOrEqualTo(Boolean value) {
            addCriterion("finish <=", value, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishIn(List<Boolean> values) {
            addCriterion("finish in", values, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishNotIn(List<Boolean> values) {
            addCriterion("finish not in", values, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishBetween(Boolean value1, Boolean value2) {
            addCriterion("finish between", value1, value2, "finish");
            return (Criteria) this;
        }

        public Criteria andFinishNotBetween(Boolean value1, Boolean value2) {
            addCriterion("finish not between", value1, value2, "finish");
            return (Criteria) this;
        }

        public Criteria andSuccessIsNull() {
            addCriterion("success is null");
            return (Criteria) this;
        }

        public Criteria andSuccessIsNotNull() {
            addCriterion("success is not null");
            return (Criteria) this;
        }

        public Criteria andSuccessEqualTo(Boolean value) {
            addCriterion("success =", value, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessNotEqualTo(Boolean value) {
            addCriterion("success <>", value, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessGreaterThan(Boolean value) {
            addCriterion("success >", value, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessGreaterThanOrEqualTo(Boolean value) {
            addCriterion("success >=", value, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessLessThan(Boolean value) {
            addCriterion("success <", value, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessLessThanOrEqualTo(Boolean value) {
            addCriterion("success <=", value, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessIn(List<Boolean> values) {
            addCriterion("success in", values, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessNotIn(List<Boolean> values) {
            addCriterion("success not in", values, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessBetween(Boolean value1, Boolean value2) {
            addCriterion("success between", value1, value2, "success");
            return (Criteria) this;
        }

        public Criteria andSuccessNotBetween(Boolean value1, Boolean value2) {
            addCriterion("success not between", value1, value2, "success");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdIsNull() {
            addCriterion("accept_un_id is null");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdIsNotNull() {
            addCriterion("accept_un_id is not null");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdEqualTo(Integer value) {
            addCriterion("accept_un_id =", value, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdNotEqualTo(Integer value) {
            addCriterion("accept_un_id <>", value, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdGreaterThan(Integer value) {
            addCriterion("accept_un_id >", value, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("accept_un_id >=", value, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdLessThan(Integer value) {
            addCriterion("accept_un_id <", value, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdLessThanOrEqualTo(Integer value) {
            addCriterion("accept_un_id <=", value, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdIn(List<Integer> values) {
            addCriterion("accept_un_id in", values, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdNotIn(List<Integer> values) {
            addCriterion("accept_un_id not in", values, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdBetween(Integer value1, Integer value2) {
            addCriterion("accept_un_id between", value1, value2, "acceptUnId");
            return (Criteria) this;
        }

        public Criteria andAcceptUnIdNotBetween(Integer value1, Integer value2) {
            addCriterion("accept_un_id not between", value1, value2, "acceptUnId");
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
     * @date 2019-08-28
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