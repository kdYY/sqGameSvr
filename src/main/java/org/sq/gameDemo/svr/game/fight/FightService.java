package org.sq.gameDemo.svr.game.fight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.MonsterDeadEvent;
import org.sq.gameDemo.svr.eventManage.event.PlayerDeadEvent;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.equip.service.EquitService;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillCache;
import org.sq.gameDemo.svr.game.skills.service.SkillService;
import sun.rmi.runtime.Log;

import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class FightService {

    @Autowired
    private SkillService skillService;

    @Autowired
    private SenceService senceService;

    @Autowired
    private PlayerCache playerCache;

    @Autowired
    private EntityService entityService;

    @Autowired
    private EquitService equitService;




    public void skillAttack(Player player, Integer skillId, List<Long> targets) {
        if(targets.size() == 1) {
            skillAttackSingleTarget(player, skillId, targets.get(0));
        } else {
            skillAttackManyTarget(player, skillId, targets);
        }
    }

    /**
     * 玩家使用技能打群怪,
     * @param player
     * @param skillId
     */
    private void skillAttackSingleTarget(Player player, Integer skillId, Long targetId) {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(targetId);
        Skill skill = checkSkillState(player, skillId, longs);
        Character target =  null;
        SenceConfigMsg senecMsg = senceService.getSenecMsgById(player.getSenceId());

        if(equitService.equipCanUse(player)
                && skill != null
                && (target = findTarget(player, targetId, senecMsg)) != null) {

            //如果使用技能成功
            if(skillService.characterUseSkillAttack(player, target, skill, senecMsg)) {
                //宝宝跟随攻击
                if(player.getBaby() != null) {
                    player.getBaby().setTarget(target);
                }
                //武器损耗
                equipDurable(player);
            }

        }
    }


    /**
     * 寻怪机制
     */
    private Character findTarget(Player player, Long targetId, SenceConfigMsg senecMsg) {
        //找到目标
        Character target = null;
        //找到怪物

        target = senecMsg.getMonsterList()
                .stream()
                .filter(monster -> monster.getId().equals(targetId) && monster.getHp() > 0)
                .findFirst()
                .orElse(null);
        //可能是副本
        if(target == null && senecMsg instanceof CopyScene && ((CopyScene) senecMsg).getBoss().getId().equals(targetId)) {
            target = ((CopyScene) senecMsg).getBoss();
        }
        //目标是玩家
        if(target == null) {
            target = senecMsg.getPlayerList()
                    .stream()
                    .filter(pl -> pl.getId().equals(targetId) && pl.getHp() > 0)
                    .findFirst()
                    .orElse(null);
        }
        //不是玩家也不是怪物
        if(target == null) {
            senceService.notifyPlayerByDefault(player, "id为" + targetId + " 的攻击目标没找到");
            return null;
        }
        if(!target.getSenceId().equals(player.getSenceId())) {
            senceService.notifyPlayerByDefault(player, "id为" + targetId + " 的攻击目标跟玩家不同场景");
            return null;
        }
        //玩家是自己
        if(target.getId().equals(player.getId())) {
            senceService.notifyPlayerByDefault(player, "自己不能攻击自己");
            return null;
        }
        return target;
    }

    /**
     * 玩家使用技能打群怪,
     * @param player
     * @param skillId
     */
    public void skillAttackManyTarget(Player player, Integer skillId, List<Long> targetList) {
        Skill skill = checkSkillState(player, skillId, targetList);

        if(equitService.equipCanUse(player) && skill != null) {
            SenceConfigMsg senecMsg = senceService.getSenecMsgById(player.getSenceId());

            List<Character> targets = new CopyOnWriteArrayList<>();
            for (Long targetId : targetList) {
                //找到目标
                Character target = findTarget(player, targetId, senecMsg);
                if(target == null ) {
                    return;
                }
                targets.add(target);
            }

            if(skillService.characterUseSkillAttackManyTarget(player, targets, skill, senecMsg)) {
                //宝宝跟随攻击 宝宝不为空 同时宝宝如果
                if(player.getBaby() != null && !targets.contains(player.getBaby().getTarget())) {
                    player.getBaby().setTarget(targets.stream().findAny().get());
                }
                //武器损耗
                equipDurable(player);
            }
        }
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
     * 使用技能损耗装备
     * @param attacter
     */
    private void equipDurable(Player attacter) {
        int durable = new Random().nextInt(Constant.EQUIP_COMSUM_DURABLE);
        //随机损耗装备
        attacter.getEquipmentBar().values()
                .stream()
                .findAny()
                .ifPresent(equit -> {
                    equit.setDurable(equit.getDurable() - durable);
                    senceService.notifyPlayerByDefault(attacter, equit.getItemInfo().getName() + " 损耗 " + durable + "点");
                });
    }

    public void playerBeAttacked(Character attacter, Player player) {
        //设置怪物归属者
        senceService.notifyPlayerByDefault(player, "你正在被 id:" + attacter.getId() + ", name:" + attacter.getName() + " 攻击！");

        if(attacter instanceof Player && entityService.playerIsDead(player, attacter)) {
            senceService.notifyPlayerByDefault(attacter, player.getName() + "(id=" + player.getId() + ")被你杀死了");
            // 抛出玩家被其他玩家打死的事件
            EventBus.publish(new PlayerDeadEvent(attacter, player));
        } else {

        }
    }
}
