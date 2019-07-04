package org.sq.gameDemo.svr.game.entity.model;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2019-07-01
 */
public class OrderRule {
    private String orderName;

    private String rule;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName == null ? null : orderName.trim();
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }

    @Override
    public String toString() {
        return "OrderRule{" +
                "orderName='" + orderName + '\'' +
                ", rule='" + rule + '\'' +
                '}';
    }
}