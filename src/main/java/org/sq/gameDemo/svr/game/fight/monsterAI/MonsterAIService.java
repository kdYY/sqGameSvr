package org.sq.gameDemo.svr.game.fight.monsterAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.customException.CustomException;
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
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillCache;
import org.sq.gameDemo.svr.game.skills.service.SkillService;

import java.util.*;

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
     */
    public void monsterBeAttacked(Character attacter, Monster targetMonster) throws CustomException.RemoveFailedException{
        //设置怪物归属者
        if(targetMonster.getTarget() == null || targetMonster.getState().equals(CharacterState.LIVE)) {
            targetMonster.setTarget(attacter);
        } else if (targetMonster.getTarget() != null && targetMonster.getState().equals(CharacterState.ATTACKING.getCode())){
            if(!targetMonster.getTarget().getId().equals(attacter.getId())) {
                senceService.notifyPlayerByDefault(attacter,
                        "你攻打的"
                        + targetMonster.getName()
                        + "归属于"
                        + targetMonster.getTarget().getName());
            }
        }

        if(attacter instanceof Player && targetMonster.isDead()) {
            //从场景移除

            if(!senceService.removeMonster(targetMonster)) {
                    throw new CustomException.RemoveFailedException("怪物移除失败");
            }


            targetMonster.setDeadStatus();
            if(attacter instanceof Player) {
                Player player = (Player) attacter;
                player.addExp(targetMonster.getLevel() * 10);
                senceService.notifyPlayerByDefault(attacter, targetMonster.getName()
                                + "(id=" + targetMonster.getId()
                                + ")被你杀死了, 经验增加↑"
                                + targetMonster.getLevel() * 10
                                + "↑, 当前exp="
                                + player.getExp()
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
    public void monsterAttacking(Monster monster) throws CustomException.PlayerAlreadyDeadException {

        Character target = monster.getTarget();

        //目标为空，不攻击
        if(Objects.isNull(target)) {
            return;
        }

        //如果自己已经死了...
        if(monster.getHp() <= 0) {
            return;
        }


        if( !((Player) target).getSenceId().equals(monster.getSenceId())) {
            monster.setTarget(null);
            return;
        }


        Player player = null;
        //如果目标是玩家
        if(target instanceof Player) {
            player = (Player) target;
            //不同场景下不攻击

            if(!player.getSenceId().equals(monster.getSenceId())) {
                monster.setTarget(null);
                return;
            }
            //target死亡后不攻击
            if(entityService.playerIsDead(player,monster)) {
                //交给轮询器去处理
                throw new CustomException.PlayerAlreadyDeadException("玩家已经死完");
            }
        }

        //目标死亡，不攻击
        if (target.getHp() <=0 || target.getState() == -1) {
            monster.setTarget(null);
            return;
        }



        //攻击同场景内的角色
        if(target instanceof UserEntity) {
            //使用技能
            MonsterUseSkillAttack(monster, target);
        }
    }

    /**
     * 怪物使用技能攻击
     * @param monster
     * @param target
     * @throws Exception
     */
    private void MonsterUseSkillAttack(Monster monster, Character target) throws CustomException.PlayerAlreadyDeadException {
        synchronized (monster) {
            if(monster.getTarget() != null && monster.getTarget().getId().equals(target.getId())) {
                Optional<Skill> any = monster.getSkillInUsedMap().values()
                        .stream()
                        //过滤掉不能使用的技能编号
                        .filter(skill -> skillService.skillCanUse(monster, skill, null))
                        //随机找一个技能
                        .findAny();
                if(any.isPresent()) {
                    Skill skillChecked = any.get();
                    //如果技能使用成功
                    if(skillService.characterUseSkillAttack(
                            monster,
                            monster.getTarget(),
                            skillChecked,
                            senceService.getSenecMsgById(monster.getSenceId()))
                            ) {
                        //如果玩家被打死
                        if(target instanceof Player && entityService.playerIsDead((Player) target, monster)) {
                            throw new CustomException.PlayerAlreadyDeadException("玩家被怪物打死");
                        }
                        //如果目标是怪物
//                                        if(target instanceof Monster) {
//                                            monsterBeAttacked(monster, (Monster) target, senceService.getSenecMsgById(monster.getSenceId()), skillChecked);
//                                        }
                    }
                }
            }
        }



    }


}
