package org.sq.gameDemo.common;

import org.sq.gameDemo.common.proto.EntityTypeProto;

public enum OrderEnum {
    PING("ping", 100),
    ErrOrder("errOrder",101 ),
    SvrErr("svrErr", 102 ),
    Help("help", 103 ),
    Register("register",104 ),
    Login("login", 105 ),
    BindRole("bindRole", 106 ),
    GetRole("getRoleMsg", 107 ),
    CheckToken("checkToken", 108),
    Aoi("aoi", 109),
    Move("move",  110),
    TalkWithNpc("talkwithnpc", 111),
    BroadCast("broadCast", 112),
    Exit("exit", 113),
    SkillAttack("skillAttack", 114),
    ShowPlayer("ShowPlayer", 115),
    USE_ITEM("useitem", 116),
    USE_BUFF("usebuff", 117),
    REMONVE_ITEM("removeitem", 118),
    ADD_EQUIP("addequip", 119),
    REMOVE_EQUIP("removeequip", 120),
    SHOW_EQUIP("showEquip", 121),
    REPAIR_EQUIP("repairequip", 122),
    SHOW_BAG("showbag", 123), SHOW_COPY_SENCE("showcopy", 124), ENTER_COPY("entercopy",125 );

    private String order;
    private short orderCode;
    OrderEnum(String order, int orderCode) {
        this.order = order;
        this.orderCode = (short) orderCode;
        //this.messageLiteClazz = messageLiteClazz;
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

    //public Class getMessageLiteClazz() {
//        return messageLiteClazz;
//    }

    public short getOrderCode() {
        return orderCode;
    }

//    public static Object getMessageLiteByCode(short orderCode) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        for (OrderEnum orderEnum : OrderEnum.values()) {
//            if(orderEnum.getOrderCode() == orderCode) {
//                Constructor constructor = orderEnum.getMessageLiteClazz().getDeclaredConstructor();
//                constructor.setAccessible(true);
//                return constructor.newInstance();
//            }
//        }
//        return null;
//    }

    //    if (msg instanceof MessageProto.Msg) {
//        messageType = 0x00;
//    }
//    public static short getCodeByMessageLite(MessageLite msg) {
//        for (OrderEnum orderEnum : OrderEnum.values()) {
//            if(msg.getClass() == orderEnum.getMessageLiteClazz()) {
//                return orderEnum.getOrderCode();
//            }
//        }
//        return -1;
//    }


    public static OrderEnum getOrderEnumByOrder(String order) {
        for (OrderEnum orderEnum : OrderEnum.values()) {
            if(orderEnum.order.equals(order)) {
                return orderEnum;
            }
        }
        return ErrOrder;
    }
}
