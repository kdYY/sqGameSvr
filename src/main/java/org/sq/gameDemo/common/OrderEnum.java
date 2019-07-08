package org.sq.gameDemo.common;

import com.google.protobuf.MessageLite;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.UserProto;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum OrderEnum {
    ErrOrder("errOrder",101, MessageProto.Msg.class),
    SvrErr("svrErr", 102, MessageProto.Msg.class),
    Help("help", 103, MessageProto.Msg.class),
    Register("register",104, UserProto.User.class),
    Login("login", 105, UserProto.User.class),
    BindRole("bindRole", 106, MessageProto.Msg.class),
    GetRole("getRoleMsg", 107, MessageProto.Msg.class)
    ;

    private String order;
    private short orderCode;
    private Class messageLiteClazz;
    OrderEnum(String order, int orderCode, Class messageLiteClazz) {
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
