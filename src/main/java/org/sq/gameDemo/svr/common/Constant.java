package org.sq.gameDemo.svr.common;

/**
 * 系统常量
 */
public interface Constant {

    /**
     * 角色类型
     */
    public static String AP = "AP";
    public static String MP = "MP";

    /**
     * 攻速增益
     */
    public static double hpAttackIncreaseRate = 0.05;

    /**
     * 默认角色名称
     */
    public static String DefaultPlayerName = "玩家";

    /**
     * 新玩家默认场景id
     */
    public static Integer DEFAULT_SENCE_ID = 1;

    /**
     * 非角色单位码
     */
    public static Integer NPC = 1;

    /**
     * 怪物类型码
     */
    public static Integer Monster = 2;

    /**
     * 系统错误响应吗
     */
    public static Integer SVR_ERR = 500;

    /**
     * 指令错误响应码
     */
    public static Integer ORDER_ERR = 404;


    /**
     * 击杀怪物的经验
     */
    public static Integer MONSTER_EXP = 10;

    /**
     * 等级除数
     */
    public static Integer LEVEL_DIVISOR = 100;

    public static int SUCCESS = 200;

    int EQUIP_COMSUM_DURABLE = 5;
    /**
     * 元宝代号
     */
    Integer YUAN_BAO = 1000;
}
