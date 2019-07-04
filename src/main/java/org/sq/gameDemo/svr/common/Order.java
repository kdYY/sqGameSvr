package org.sq.gameDemo.svr.common;

public enum Order {

    Site("site"),
    ErrOrder("errOrder");
    ;
    private String order;

    private Order(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }
}
