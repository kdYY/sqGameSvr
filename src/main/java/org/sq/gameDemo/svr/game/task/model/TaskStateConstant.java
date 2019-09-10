package org.sq.gameDemo.svr.game.task.model;

public class TaskStateConstant {
    /**0 不可接状态*/
    public static final int CANNOT_ACCEPT = 0;

    /**1 可接  但还未接的状态*/
    public static final int CAN_ACCEPT = 1;

    /**2 已接  正在进行中*/
    public static final int DOING = 2;

    /**3 完成  未领奖*/
    public static final int COMPLETE = 3;

    /**4 完成  已领奖*/
    public static final int FINISH = 4;
}
