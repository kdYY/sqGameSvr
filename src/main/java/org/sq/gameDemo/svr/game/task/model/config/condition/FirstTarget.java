package org.sq.gameDemo.svr.game.task.model.config.condition;

public enum FirstTarget {

    ADD_FIREND(1, "第一次加好友"),
    ADD_TEAM(2, "第一次加入队伍"),
    ADD_GUILD(3, "第一次加入公会"),
    FINISH_TRADE(4, "第一次完成交易"),
    FINISH_PK(5, "第一次完成PK"),
    FIRST_MAIL(6, "第一次发送邮件"),
    FIRST_STORE(7, "第一次在商店购买")

    ;
    private int code;
    private String meaning;

    FirstTarget(int code, String meaning) {
        this.code = code;
        this.meaning = meaning;
    }

    public int getCode() {
        return code;
    }
}
