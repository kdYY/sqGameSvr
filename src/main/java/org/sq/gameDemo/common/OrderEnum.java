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
    SHOW_BAG("showbag", 123),
    SHOW_COPY_SENCE("showcopy", 124),
    ENTER_NEW_COPY("enternewcopy",125),
    CHAT("chat", 126),
    TALK_TO_WORD("talktoword", 127),
    SEND_MAIL("sendMail", 128),
    SHOW_ALL_MAIL("showAllMail", 129),
    RECEIVE_ALL_MAIL("receiveAllMail", 130),
    GET_MAIL("getMail", 131),
    SHOW_STORE("showStore", 132),
    BUY_SHOP_ITEM("buyshopItem", 133),
    TIDY_BAG("tidybag", 134),
    SHOW_EXIST_COPY_SENCE("showCopySenceExist", 135),
    ENTER_COPY("entercopy", 136),
    RECEIVE_MAIL("receiveMail", 137),
    SHOW_ITEMINFO("showItemInfo", 138),
    START_ONLINE_TRADE("startonlineTrade", 139),
    ACCEPT_ONLINE_TRADE("acceptonlinetrade", 140),
    GET_ONLINE_TRADE("getonlinetrade", 150),
    START_DEAL_TRADE("startdealTrade", 151),
    ACCEPT_DEAL_TRADE("acceptDealTrade",152 ),
    SHOW_DEAL("showDeal", 153),
    GET_DEAL_CAN_BUY("canbuyDeal", 154 ),
    GET_DEAL_HISTORY("getDealHistory", 155),
    GET_ONLINE_TRADE_CAN_RECEIVE("getOnlineTradeCnReceive", 156),
    GET_ONLINE_TRADE_HISTORY("getOnlineTradeHistory", 157),

    CREATE_GUILD("createGuild", 158),
    SHOW_GUILD_CAN_ATTEND("showGuildCanAttend", 159),
    APPLY_ATTEND_GUILD("applyAttendGuild", 160),
    SHOW_GUILD_REQUEST("showGuildReq", 161),
    AGREE_ATTEND_REQUEST("agreeAttendRequest", 162),
    EXIT_GUILD("exitGuild", 163),
    DONATE_ITEM("donateItem", 164),
    GET_GUILD_ITEM("getGuildItem", 165),
    SHOW_CHAIRMAN_GUILD("showChairManGuild", 166),
    SHOW_GUILDLIST("showGuildList", 167),
    SHOW_GUILD_BAG("showGuildWareHouse", 168),
    SHOW_TASK_CAN_ACCEPT("showTaskCanAccpet", 169),
    SHOW_TASK("showtask", 170),
    ACCEPT_TASK("acceptTask", 171),
    CREATE_TEAM("createTeam", 172),
    INVITE_TEAM("inviteTeam", 173),
    EXIT_TEAM("exitTeam", 174),
    SHOW_TEAM("showTeam", 175),
    ENTER_TEAM("enterTeam", 176),
    ADD_FRIEND("addFriend", 177),
    SHOW_FRIEND("showFriend", 178),
    ;


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
