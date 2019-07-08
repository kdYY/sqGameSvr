package org.sq.gameDemo.common;

import com.google.protobuf.MessageLite;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.UserProto;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum OrderEnum {

    Site("site",100, MessageProto.Msg.class),
    ErrOrder("errOrder",101, MessageProto.Msg.class),
    Register("register",102, UserProto.User.class),
    SvrErr("svrErr", 103, MessageProto.Msg.class),
    Login("login", 104, UserProto.User.class);
    private String order;
    private short orderCode;
    private Class messageLiteClazz;
    private OrderEnum(String order, int orderCode, Class messageLiteClazz) {
        this.order = order;
        this.orderCode = (short) orderCode;
        this.messageLiteClazz = messageLiteClazz;
    }

    public static short getOrderCode(String order) {
        for (OrderEnum orderEnum : OrderEnum.values()) {
            if(orderEnum.order.equals(order)) {
                return orderEnum.orderCode;
            }
        }
        return -1;
    }

    public String getOrder() {
        return order;
    }


    public static String getOrderByCode(short orderCode) {
        for (OrderEnum orderEnum : OrderEnum.values()) {
            if(orderEnum.orderCode == orderCode) {
                return orderEnum.order;
            }
        }
        return null;
    }

    public Class getMessageLiteClazz() {
        return messageLiteClazz;
    }

    public short getOrderCode() {
        return orderCode;
    }

    public static Object getMessageLiteByCode(short orderCode) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (OrderEnum orderEnum : OrderEnum.values()) {
            if(orderEnum.getOrderCode() == orderCode) {
                Constructor constructor = orderEnum.getMessageLiteClazz().getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            }
        }
        return null;
    }

    //    if (msg instanceof MessageProto.Msg) {
//        messageType = 0x00;
//    }
    public static short getCodeByMessageLite(MessageLite msg) {
        for (OrderEnum orderEnum : OrderEnum.values()) {
            if(msg.getClass() == orderEnum.getMessageLiteClazz()) {
                return orderEnum.getOrderCode();
            }
        }
        return -1;
    }


    public static OrderEnum getOrderEnumByOrder(String order) {
        for (OrderEnum orderEnum : OrderEnum.values()) {
            if(orderEnum.order.equals(order)) {
                return orderEnum;
            }
        }
        return ErrOrder;
    }
}
