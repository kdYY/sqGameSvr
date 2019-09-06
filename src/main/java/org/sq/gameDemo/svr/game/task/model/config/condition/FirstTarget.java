package org.sq.gameDemo.svr.game.task.model.config.condition;

public enum FirstTarget {

    FIRST_ADD_FIREND(1, "第一次加好友"),
    FIRST_ADD_TEAM(2, "第一次加入队伍"),
    FIRST_ADD_GUILD(3, "第一次加入公会"),
    FIRST_FINISH_TRADE(4, "第一次完成交易"),
    FIRST_FINISH_PK(5, "第一次完成PK"),

    ;
    private int code;
    private String meaning;

    FirstTarget(int code, String meaning) {
        this.code = code;
        this.meaning = meaning;
    }
}
