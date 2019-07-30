package org.sq.gameDemo.svr.game.fight.monsterAI.state;

import io.netty.channel.Channel;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;

@FunctionalInterface
public interface MonsterState {
    void handle(Character character, Channel channel) throws Exception;
}
