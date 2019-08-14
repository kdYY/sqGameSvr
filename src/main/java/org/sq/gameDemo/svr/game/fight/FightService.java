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
import org.sq.gameDemo.svr.game.equip.service.EquitService;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillCache;
import org.sq.gameDemo.svr.game.skills.service.SkillService;

import java.lang.annotation.Target;
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
    private EntityService entityService;

    @Autowired
    private EquitService equitService;


    /**
     * 玩家使用技能打群怪,
     * @param player
     * @param skillId
     * @param targetIdList
     */
    public void skillAttackManyTarget(Player player, Integer skillId, List<Long> targetIdList) {
        Skill skill = checkSkillState(player, skillId, targetIdList);

        if(equitService.equipCanUse(player) && skill != null) {
            targetIdList.forEach(targetId -> {
                //找到目标
                Character target = null;
                //找到怪物
                SenceConfigMsg senecMsg = senceService.getSenecMsgById(player.getSenceId());
                target = senecMsg.getMonsterList()
                        .stream()
                        .filter(monster -> monster.getId().equals(targetId) && monster.getHp() > 0)
                        .findFirst()
                        .orElse(null);
                if(target == null) {
                    target = senecMsg.getPlayerList()
                            .stream()
                            .filter(pl -> pl.getId().equals(targetId) && pl.getHp() > 0)
                            .findFirst()
                            .orElse(null);
                }
                if(target == null) {
                    senceService.notifyPlayerByDefault(player, "id为" + targetId + " 的攻击目标没找到");
                    return;
                }
                if(target.getId().equals(player.getId())) {
                    senceService.notifyPlayerByDefault(player, "自己不能攻击自己");
                    return;
                }
                else {
                    skillAttackSingleTarget(player, target, skill, senecMsg);
                }
            });
        }
    }


    public void skillAttackSingleTarget(Player player, Integer skillId, Long targetId) {
        ArrayList<Long> targetIdList = new ArrayList<>();
        targetIdList.add(targetId);
        skillAttackManyTarget(player, skillId, targetIdList);
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
     * 玩家使用技能打单体目标
     * @param player
     * @param skill
     */
    private void skillAttackSingleTarget(Player player, Character target, Skill skill, SenceConfigMsg senecMsg) {

        if(Objects.isNull(target)) {
            playerCache.getChannelByPlayerId(player.getId())
                    .writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity("目标id不存在,该目标是npc不可攻击体,或者该怪物已死亡，请检查怪物id"));
        } else {
            //如果使用技能成功
            if(skillService.characterUseSkillAttack(player, target, skill, senecMsg)) {
                //武器损耗
                equipDurable(player);
            }

        }
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
        }
    }
}
