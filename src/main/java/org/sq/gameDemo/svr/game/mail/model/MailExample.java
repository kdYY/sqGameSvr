package org.sq.gameDemo.svr.game.mail.model;

import java.util.ArrayList;
import java.util.List;

public class MailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MailExample() {
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
     * @date 2019-08-22
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

        public Criteria andSenderUnIdIsNull() {
            addCriterion("sender_un_id is null");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdIsNotNull() {
            addCriterion("sender_un_id is not null");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdEqualTo(Integer value) {
            addCriterion("sender_un_id =", value, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdNotEqualTo(Integer value) {
            addCriterion("sender_un_id <>", value, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdGreaterThan(Integer value) {
            addCriterion("sender_un_id >", value, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("sender_un_id >=", value, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdLessThan(Integer value) {
            addCriterion("sender_un_id <", value, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdLessThanOrEqualTo(Integer value) {
            addCriterion("sender_un_id <=", value, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdIn(List<Integer> values) {
            addCriterion("sender_un_id in", values, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdNotIn(List<Integer> values) {
            addCriterion("sender_un_id not in", values, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdBetween(Integer value1, Integer value2) {
            addCriterion("sender_un_id between", value1, value2, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andSenderUnIdNotBetween(Integer value1, Integer value2) {
            addCriterion("sender_un_id not between", value1, value2, "senderUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdIsNull() {
            addCriterion("recevier_un_id is null");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdIsNotNull() {
            addCriterion("recevier_un_id is not null");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdEqualTo(Integer value) {
            addCriterion("recevier_un_id =", value, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdNotEqualTo(Integer value) {
            addCriterion("recevier_un_id <>", value, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdGreaterThan(Integer value) {
            addCriterion("recevier_un_id >", value, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("recevier_un_id >=", value, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdLessThan(Integer value) {
            addCriterion("recevier_un_id <", value, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdLessThanOrEqualTo(Integer value) {
            addCriterion("recevier_un_id <=", value, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdIn(List<Integer> values) {
            addCriterion("recevier_un_id in", values, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdNotIn(List<Integer> values) {
            addCriterion("recevier_un_id not in", values, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdBetween(Integer value1, Integer value2) {
            addCriterion("recevier_un_id between", value1, value2, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andRecevierUnIdNotBetween(Integer value1, Integer value2) {
            addCriterion("recevier_un_id not between", value1, value2, "recevierUnId");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("time is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("time is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Long value) {
            addCriterion("time =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Long value) {
            addCriterion("time <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Long value) {
            addCriterion("time >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("time >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Long value) {
            addCriterion("time <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Long value) {
            addCriterion("time <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Long> values) {
            addCriterion("time in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Long> values) {
            addCriterion("time not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Long value1, Long value2) {
            addCriterion("time between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Long value1, Long value2) {
            addCriterion("time not between", value1, value2, "time");
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

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andContentIsNull() {
            addCriterion("content is null");
            return (Criteria) this;
        }

        public Criteria andContentIsNotNull() {
            addCriterion("content is not null");
            return (Criteria) this;
        }

        public Criteria andContentEqualTo(String value) {
            addCriterion("content =", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotEqualTo(String value) {
            addCriterion("content <>", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThan(String value) {
            addCriterion("content >", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThanOrEqualTo(String value) {
            addCriterion("content >=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThan(String value) {
            addCriterion("content <", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThanOrEqualTo(String value) {
            addCriterion("content <=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLike(String value) {
            addCriterion("content like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotLike(String value) {
            addCriterion("content not like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentIn(List<String> values) {
            addCriterion("content in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotIn(List<String> values) {
            addCriterion("content not in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentBetween(String value1, String value2) {
            addCriterion("content between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotBetween(String value1, String value2) {
            addCriterion("content not between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andItemsStrIsNull() {
            addCriterion("items_str is null");
            return (Criteria) this;
        }

        public Criteria andItemsStrIsNotNull() {
            addCriterion("items_str is not null");
            return (Criteria) this;
        }

        public Criteria andItemsStrEqualTo(String value) {
            addCriterion("items_str =", value, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrNotEqualTo(String value) {
            addCriterion("items_str <>", value, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrGreaterThan(String value) {
            addCriterion("items_str >", value, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrGreaterThanOrEqualTo(String value) {
            addCriterion("items_str >=", value, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrLessThan(String value) {
            addCriterion("items_str <", value, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrLessThanOrEqualTo(String value) {
            addCriterion("items_str <=", value, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrLike(String value) {
            addCriterion("items_str like", value, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrNotLike(String value) {
            addCriterion("items_str not like", value, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrIn(List<String> values) {
            addCriterion("items_str in", values, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrNotIn(List<String> values) {
            addCriterion("items_str not in", values, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrBetween(String value1, String value2) {
            addCriterion("items_str between", value1, value2, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andItemsStrNotBetween(String value1, String value2) {
            addCriterion("items_str not between", value1, value2, "itemsStr");
            return (Criteria) this;
        }

        public Criteria andIsReadIsNull() {
            addCriterion("is_read is null");
            return (Criteria) this;
        }

        public Criteria andIsReadIsNotNull() {
            addCriterion("is_read is not null");
            return (Criteria) this;
        }

        public Criteria andIsReadEqualTo(Boolean value) {
            addCriterion("is_read =", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadNotEqualTo(Boolean value) {
            addCriterion("is_read <>", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadGreaterThan(Boolean value) {
            addCriterion("is_read >", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_read >=", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadLessThan(Boolean value) {
            addCriterion("is_read <", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadLessThanOrEqualTo(Boolean value) {
            addCriterion("is_read <=", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadIn(List<Boolean> values) {
            addCriterion("is_read in", values, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadNotIn(List<Boolean> values) {
            addCriterion("is_read not in", values, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadBetween(Boolean value1, Boolean value2) {
            addCriterion("is_read between", value1, value2, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_read not between", value1, value2, "isRead");
            return (Criteria) this;
        }

        public Criteria andSenderNameIsNull() {
            addCriterion("sender_name is null");
            return (Criteria) this;
        }

        public Criteria andSenderNameIsNotNull() {
            addCriterion("sender_name is not null");
            return (Criteria) this;
        }

        public Criteria andSenderNameEqualTo(String value) {
            addCriterion("sender_name =", value, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameNotEqualTo(String value) {
            addCriterion("sender_name <>", value, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameGreaterThan(String value) {
            addCriterion("sender_name >", value, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameGreaterThanOrEqualTo(String value) {
            addCriterion("sender_name >=", value, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameLessThan(String value) {
            addCriterion("sender_name <", value, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameLessThanOrEqualTo(String value) {
            addCriterion("sender_name <=", value, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameLike(String value) {
            addCriterion("sender_name like", value, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameNotLike(String value) {
            addCriterion("sender_name not like", value, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameIn(List<String> values) {
            addCriterion("sender_name in", values, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameNotIn(List<String> values) {
            addCriterion("sender_name not in", values, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameBetween(String value1, String value2) {
            addCriterion("sender_name between", value1, value2, "senderName");
            return (Criteria) this;
        }

        public Criteria andSenderNameNotBetween(String value1, String value2) {
            addCriterion("sender_name not between", value1, value2, "senderName");
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
     * @date 2019-08-22
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