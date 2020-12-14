package org.sq.gameDemo.aov;

import org.sq.gameDemo.svr.game.characterEntity.model.Player;

public interface IPlayerDataOperate {
    //IO线程执行 领域对象的构建
    void initEntity(Player player);
    // 玩家额外数据初始化 在initEntity后在玩家线程执行
    void initData(Player player);
    void updatePlayer(Player player); //延迟插入更新
}
