package org.sq.gameDemo.svr.game.fight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.bag.service.EquitService;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillCache;
import org.sq.gameDemo.svr.game.skills.service.SkillService;

import java.util.*;

@Component
public class FightService {

    @Autowired
    private SkillService skillService;

    @Autowired
    private SenceService senceService;

    @Autowired
    private PlayerCache playerCache;

    @Autowired
    private SkillCache skillCache;

    @Autowired
    private EquitService equitService;


    /**
     * 玩家使用技能打群怪,
     * @param player
     * @param skillId
     * @param targetIdList
     */
    public void skillAttackManyMonster(Player player, Integer skillId, List<Long> targetIdList) {
        Skill skill = checkSkillState(player, skillId, targetIdList);

        if(equitService.equipCanUse(player) && skill != null) {
            targetIdList.forEach(monsterId -> {
                //玩家攻打怪物
                skillAttackSingleMonster(player, monsterId, skill);
            });
        }
    }



    public void skillAttackSingleMonster(Player player, Integer skillId, Long targetId) {
        ArrayList<Long> targetIdList = new ArrayList<>();
        targetIdList.add(targetId);
        skillAttackManyMonster(player, skillId, targetIdList);
    }


    /**
     * 检查技能状态
     * @param player
     * @param skillId
     * @param targetIdList
     */
    private Skill checkSkillState(Player player,  Integer skillId, List<Long> targetIdList) {
        Skill skill = player.getSkillInUsedMap().get(skillId);

        if(!skillService.skillCanUse(player, skill, targetIdList)) {
            return null;
        }
        return  skill;
    }

    /**
     * 玩家使用技能打单体怪物
     * @param player
     * @param monsterId
     * @param skill
     */
    private void skillAttackSingleMonster(Player player, Long monsterId, Skill skill) {
        SenceConfigMsg senecMsg = senceService.getSenecMsgById(player.getSenceId());
        //找到怪物
        Monster targetMonster = senecMsg.getMonsterList()
                .stream()
                .filter(monster -> monster.getId().equals(monsterId) && monster.getHp() > 0)
                .findFirst()
                .orElse(null);


        if(Objects.isNull(targetMonster)) {
            playerCache.getChannelByPlayerId(player.getId())
                    .writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity("目标id不存在,该目标是npc不可攻击体,或者该怪物已死亡，请检查怪物id"));
        } else {
            //如果使用技能成功
            if(skillService.characterUseSkillAttack(player, targetMonster, skill, senecMsg)) {
//                player.setTarget(targetMonster);
//
//                //包装成一个任务，扔进去单线程顺序消费
//                TimeTaskManager.singleThreadSchedule(skill.getCastTime() <= 0 ? 0: skill.getCastTime() ,
//                        () -> {
//                            System.out.println("222");
//                            System.out.println(target.getId() + ":" + target.getName() + ",hp:" + target.getHp());
//                            if(future.isDone()) {
//                                monsterAIService.monsterBeAttacked(player, targetMonster, senecMsg, skill);
//                            }
//                            if(targetMonster.getHp() <= 0) {
//                                System.out.println("333");
//                            }
//                });
            }

        }

    }
}
