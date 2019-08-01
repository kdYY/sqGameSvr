package org.sq.gameDemo.svr.game.fight.monsterAI.state;

import io.netty.channel.Channel;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;

/**
 * 怪物状态接口
 */
@FunctionalInterface
public interface MonsterStateHandle {
    void handle(Monster monster) throws Exception;
}
