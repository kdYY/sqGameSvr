package org.sq.gameDemo.svr.game.fight.monsterAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.MonsterBeAttackedEvent;
import org.sq.gameDemo.svr.eventManage.event.MonsterDeadEvent;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillCache;
import org.sq.gameDemo.svr.game.skills.service.SkillService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Component
public class MonsterAIService {

    @Autowired
    private SenceService senceService;
    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private SkillCache skillCache;
    @Autowired
    private SkillService skillService;
    @Autowired
    private EntityService entityService;

    /**
     * 怪物被攻击
     * @param attacter
     * @param targetMonster
     * @param senecMsg
     * @param skill
     */
    public void monsterBeAttacked(Character attacter, Monster targetMonster, SenceConfigMsg senecMsg, Skill skill) {
        //设置怪物归属者
        if(targetMonster.getTarget() == null) {
            targetMonster.setTarget(attacter);
        } else {
            playerCache.getChannelByPlayerId(attacter.getId()).writeAndFlush(
                    ProtoBufUtil.getBroadCastDefaultEntity(
                            "你攻打的"
                            + targetMonster.getName()
                            + "归属于"
                            + targetMonster.getTarget().getName()
                    )
            );
        }

        //怪物被攻击事件
        EventBus.publish(new MonsterBeAttackedEvent(attacter, targetMonster, senecMsg, skill));


        if(attacter instanceof Player && targetMonster.isDead()) {
            targetMonster.setDeadStatus();
            if(attacter instanceof Player) {
                Player player = (Player) attacter;
                player.addExp(Constant.MONSTER_EXP);
                playerCache.getChannelByPlayerId(attacter.getId()).writeAndFlush(
                        ProtoBufUtil.getBroadCastDefaultEntity(targetMonster.getName()
                                + "(id=" + targetMonster.getId()
                                + ")被你杀死了, 经验增加"
                                + Constant.MONSTER_EXP
                                + "↑,当前exp="
                                + player.getExp()
                        )
                );
            }
            // 抛出怪物被玩家打死的事件
            EventBus.publish(new MonsterDeadEvent(attacter, targetMonster));
        }
    }


    /**
     * 怪物攻击目标
     * @param monster
     */
    public void monsterAttacking(Monster monster) {

        Character target = monster.getTarget();

        //目标为空，不攻击
        if(Objects.isNull(target)) {
            return;
        }

        Player player = null;
        //如果目标是玩家
        if(target instanceof Player) {
            player = (Player) target;
            //不同场景下不攻击

            if(!player.getSenceId().equals(monster.getSenceId())) {
                monster.setTarget(null);
                monster.setState(CharacterState.LIVE.getCode());
                return;
            }
            //target死亡后不攻击
            if(entityService.playerIsDead(player,monster)) {
                //交给事件去处理
                return;
            }
        }

        //目标死亡，不攻击
        if (target.getHp() <=0 || target.getState() == -1) {
            monster.setTarget(null);
            monster.setState(CharacterState.LIVE.getCode());
            return;
        }


        //攻击同场景内的角色
        if(target instanceof UserEntity && ((Player) target).getSenceId().equals(monster.getSenceId())) {
            //使用普攻
            try {
                MonsterUseSkillAttack(monster, target);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 怪物使用技能攻击
     * @param monster
     * @param target
     * @throws Exception
     */
    private void MonsterUseSkillAttack(Monster monster, Character target) throws Exception {
        synchronized (monster) {
            if(monster.getTarget() != null && monster.getTarget().getId().equals(target.getId())) {
                Arrays.stream(monster.getSkillStr().split(","))
                        .map(Integer::valueOf)
                        .map(skId -> skillCache.get(skId))
                        //过滤掉不能使用的技能编号
                        .filter(skill -> skillService.skillCanUse(monster, skill, new ArrayList<>()))
                        //随机找一个技能
                        .findAny()
                        .ifPresent(
                                skillChecked -> {
                                    //如果技能使用成功
                                    if(skillService.characterUseSkillAttack(
                                            monster,
                                            monster.getTarget(),
                                            skillChecked,
                                            senceService.getSenecMsgById(monster.getSenceId()))
                                            ) {
                                        //如果玩家被打死
                                        if(target instanceof Player && entityService.playerIsDead((Player) target, monster)) {
                                            return;
                                        }
                                        //如果目标是怪物
                                        if(target instanceof Monster) {
                                            monsterBeAttacked(monster, (Monster) target, senceService.getSenecMsgById(monster.getSenceId()), skillChecked);
                                        }
                                    }
                                }
                        );
            }
        }



    }


}
