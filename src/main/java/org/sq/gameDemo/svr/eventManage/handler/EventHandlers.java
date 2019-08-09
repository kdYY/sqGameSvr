package org.sq.gameDemo.svr.eventManage.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.LevelEvent;
import org.sq.gameDemo.svr.eventManage.event.MonsterBeAttackedEvent;
import org.sq.gameDemo.svr.eventManage.event.MonsterDeadEvent;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.drop.service.DropPool;
import org.sq.gameDemo.svr.game.fight.monsterAI.MonsterAIService;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.Optional;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2019/1/2 15:32
 * @version 1.00
 * Description: 等级事件处理
 */
@Slf4j
@Component
public class EventHandlers {

    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private DropPool dropPool;
    @Autowired
    private SenceService senceService;
    @Autowired
    private MonsterAIService monsterAIService;

    {
        EventBus.registe(LevelEvent.class, this::levelUp);
        log.info("角色升级事件注册成功");
        EventBus.registe(MonsterBeAttackedEvent.class, this::monsterBeAttacked);
        log.info("怪物被攻击事件注册成功");
        EventBus.registe(MonsterDeadEvent.class, this::monsterDead);
        log.info("怪物死亡事件注册成功");
    }

    /**
     * 玩家升级事件处理
     * @param levelEvent
     */
    private  void levelUp(LevelEvent levelEvent) {
        Optional.ofNullable(levelEvent.getPlayer()).ifPresent(player -> {
            player.setLevel(levelEvent.getNewlevel());

            playerCache.getChannelByPlayerId(player.getId()).writeAndFlush(
                    ProtoBufUtil.getBroadCastDefaultEntity("恭喜你升了一级，目前等级是" + player.getLevel()));
        });
    }


    /**
     * 怪物被攻击事件处理
     * @param attackedEvent
     */
    private  void monsterBeAttacked(MonsterBeAttackedEvent attackedEvent) {
        //怪物被攻击
        Monster targetMonster = attackedEvent.getTargetMonster();
        Character attacter = attackedEvent.getAttacter();

        //再做一次校验
        if(targetMonster != null &&((UserEntity)attacter).getTarget().getId().equals(targetMonster.getId())) {
            targetMonster.setState(CharacterState.ATTACKING.getCode());
            // TODO 调试通放开
            // monsterAIService.monsterAttacking(targetMonster);
        }


    }

    /**
     * 怪物死亡事件处理 注意，不在这设置monster的属性
     * @param monsterDeadEvent
     */
    private  void monsterDead(MonsterDeadEvent monsterDeadEvent) {
        //玩家 检查任务进度，掉落物品，触发幸运大爆 ....
        dropPool.getDropItems(monsterDeadEvent.getAttacter(), monsterDeadEvent.getTargetMonster());
    }

}
