package org.sq.gameDemo.common;

public enum OrderEnum {

    Site("site",100),
    ErrOrder("errOrder",101)
    ;
    private String order;
    private short orderCode;

    private OrderEnum(String order, int orderCode) {
        this.order = order;
        this.orderCode = (short) orderCode;
    }

    public static short getOrderCode(String order) {
        for (OrderEnum orderEnum : OrderEnum.values()) {
            if(orderEnum.order.equals(order)) {
                return orderEnum.orderCode;
            }
        }
        return 0;
    }

    public String getOrder() {
        return order;
    }

    public static String getOrder(short orderCode) {
        for (OrderEnum orderEnum : OrderEnum.values()) {
            if(orderEnum.orderCode == orderCode) {
                return orderEnum.order;
            }
        }
        return null;
    }

    public short getOrderCode() {
        return orderCode;
    }
}
