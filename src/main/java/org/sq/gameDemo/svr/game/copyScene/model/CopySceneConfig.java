package org.sq.gameDemo.svr.game.copyScene.model;

import lombok.Data;

/**
 * 场景配置读取类
 */
@Data
public class CopySceneConfig {
    private int id;

    private String name;
    //副本允许容纳的玩家个数
    private Integer limit;

    private String monsters;


    private String boss;
    //副本计时
    private Long  time;

    //副本奖励经验
    private Long exp;
}
