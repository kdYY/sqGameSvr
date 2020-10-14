package org.sq.gameDemo.observer.Impl;

import org.sq.gameDemo.observer.IObserver;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;

public interface ICreateureDead extends IObserver {
    /**
     * 生物死亡
     * @param creature 生物
     * @param lastAttacker 最后攻击者 可能为null
     */
    void onCreatureDead(Character creature, Character lastAttacker);
}
