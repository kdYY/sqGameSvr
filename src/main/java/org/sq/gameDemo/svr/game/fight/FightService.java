package org.sq.gameDemo.svr.game.fight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillCache;
import org.sq.gameDemo.svr.game.skills.service.SkillService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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


    /**
     * 玩家使用技能打群怪
     * @param player
     * @param skillId
     * @param targetIdList
     */
    public void skillAttackManyMonster(Player player, Integer skillId, List<Long> targetIdList) {
        Skill skill = checkSkillState(player, skillId, targetIdList);

        Optional.ofNullable(skill).ifPresent(
                o -> targetIdList.forEach(monsterId -> skillAttackSingleMonster(player, monsterId, o))
        );


    }

    /**
     * 检查技能状态
     * @param player
     * @param skillId
     * @param targetIdList
     */
    private Skill checkSkillState(Player player,  Integer skillId, List<Long> targetIdList) {
        Skill skill = skillCache.get(skillId);
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
                .filter(monster -> monster.getId().equals(monsterId))
                .findFirst()
                .orElse(null);

        if(Objects.isNull(targetMonster)) {
            playerCache.getChannelByPlayerId(player.getId())
                    .writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity("目标怪物不存在，请检查怪物id"));
        } else {
            skillService.characterUseSkillAttack(player, targetMonster, skill, senecMsg);
        }

    }
}
