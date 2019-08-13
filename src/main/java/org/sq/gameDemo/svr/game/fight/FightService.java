package org.sq.gameDemo.svr.game.fight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.equip.service.EquitService;
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
                } else {
                    skillAttackSingleTarget(player, target, skill, senecMsg);
                }
            });
        }
    }


    public void skillAttackSingleMonster(Player player, Integer skillId, Long targetId) {
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
     * 玩家使用技能打单体怪物
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

    private void equipDurable(Player attacter) {
        for (Item item : attacter.getEquipmentBar().values()) {
            int durable = new Random().nextInt(Constant.EQUIP_COMSUM_DURABLE);
            item.setDurable(item.getDurable() - durable);
            senceService.notifyPlayerByDefault(attacter, item.getItemInfo().getName() + " 损耗 " + durable + "点");
        }
    }

}
