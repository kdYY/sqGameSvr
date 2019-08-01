package org.sq.gameDemo.svr.game.fight.monsterAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.MonsterBeAttackedEvent;
import org.sq.gameDemo.svr.eventManage.event.MonsterDeadEvent;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;

@Component
public class MonsterAIService {

    @Autowired
    private SenceService senceService;

    @Autowired
    private PlayerCache playerCache;

    /**
     * 怪物被攻击
     * @param attacter
     * @param targetMonster
     * @param senecMsg
     * @param skill
     */
    public void monsterBeAttacked(Character attacter, Monster targetMonster, SenceConfigMsg senecMsg, Skill skill) {
        //此时怪物正在攻击其他玩家
        if(targetMonster.getTarget() != null && attacter instanceof UserEntity && !targetMonster.getTarget().equals(attacter)) {
            playerCache.getChannelByPlayerId(attacter.getId()).writeAndFlush(
                    ProtoBufUtil.getBroadCastDefaultEntity("你攻击的"
                            + targetMonster.getName()
                            + "(id=" + targetMonster.getId()
                            + ") 正在攻击"
                            + targetMonster.getTarget().getName()));
            return;
        }
        //怪物被攻击事件
        EventBus.publish(new MonsterBeAttackedEvent(attacter, targetMonster, senecMsg, skill));

        // 怪物被玩家打死的事件
        if(attacter instanceof Player && targetMonster.isDead()) {
            EventBus.publish(new MonsterDeadEvent(attacter, targetMonster, senecMsg, skill));
        }
    }


}
