package org.sq.gameDemo.svr.game.skills.service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.sq.gameDemo.svr.common.TimeTaskManager;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.buff.service.BuffService;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.fight.monsterAI.MonsterAIService;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.model.SkillRange;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;

@Slf4j
@Service
public class SkillService {

    @Autowired
    private SenceService senceService;
    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MonsterAIService monsterAIService;
    @Autowired
    private SkillRangeService skillRangeService;
    @Autowired
    private BuffService buffService;
    @Autowired
    private SkillCache skillCache;


    /**
     * 场景单位使用技能打另一个场景单位
     * @param attacter
     * @param targeter
     * @param skill
     * @param senecMsg
     */
    public boolean characterUseSkillAttack(Character attacter, Character targeter, Skill skill, SenceConfigMsg senecMsg, Future future) {

        if(targeter instanceof Npc) {
            if(attacter instanceof UserEntity ) {
                senceService.notifyPlayerByDefault(attacter, "npc不能被砍...");
                return false;
            }
            return false;

        }

        String content = attacter.getName() + "(id=" + attacter.getId() + ")开始使用技能"
                + skill.getName() + "攻击" + targeter.getName()
                + "(id=" + targeter.getId() + ")";

        log.debug(content);

        //开启cd
        makeSkillCD(attacter, skill);
        //技能若是有释放时间，则延迟释放

        if(attacter instanceof UserEntity && skill.getCastTime() > 0) {
            UserCache.broadcastChannelGroupBysenceId(senecMsg.getSenceId(),
                    attacter.getName() + "开始释放技能，需要" + skill.getCastTime()/1000 + "秒");
        }

        //单线程执行
        future = TimeTaskManager.singleThreadSchedule(skill.getCastTime() <= 0 ? 0 : skill.getCastTime(),
                () -> {
                    senecMsg.getSingleThreadSchedule().execute(
                            () -> {
                                if (targeter.getHp() > 0) {
                                    senceService.notifyPlayerByDefault(targeter, content);
                                    skillRangeService.routeSkill(attacter, targeter, skill, senecMsg);

                                    if(skill.getBuff() != null && skill.getBuff() != 0) {
                                        Optional.ofNullable(skill.getBuff()).ifPresent(
                                                buff -> buffService.buffAffecting(targeter, buffService.getBuff(buff))
                                        );
                                    }

                                    if (attacter instanceof Player) {
                                        ((Player) attacter).setTarget(targeter);
                                        //随机损耗装备
                                        ((Player) attacter).getEquipmentBar().values()
                                                .stream()
                                                .findAny()
                                                .ifPresent(equit -> equit.setDurable(equit.getDurable() - (int) Math.random() * 5));
                                    }
                                    if (targeter instanceof Monster) {
                                        monsterAIService.monsterBeAttacked(attacter, (Monster) targeter, senecMsg, skill);
                                    }
                                }
                            }
                    );
                });
        return true;

    }


    public boolean characterUseSkillAttack(Character attacter, Character targeter, Skill skill, SenceConfigMsg senecMsg) {
        Future future = null;
        return characterUseSkillAttack(attacter, targeter, skill, senecMsg, future);
    }

        /**
         * 使用技能前使技能进入cd
         * @param attacter
         * @param skill
         */
    private void makeSkillCD(Character attacter, Skill skill) {
        //设置技能使用时间
        skill.setLastUseTime(System.currentTimeMillis());
    }


    /**
     * 判断技能是否能使用
     * @param character
     * @param skill
     * @return
     */
    public boolean skillCanUse(Character character, Skill skill, List<Long> targetIdList) {
        String notice = "";

        if(Objects.isNull(skill)) {
            notice = "技能不存在，技能id有误";
        } else if(character.getSkillInUsedMap().get(skill.getId()).getLastUseTime() + skill.getCd() > System.currentTimeMillis()) {
            notice = "技能正在冷却";
        } else if(character instanceof Player) {
            if(Objects.isNull(character.getSkillInUsedMap().get(skill.getId()))) {
                notice = "该角色对应的职业不具备此技能";
            }
            if(character.getMp() < skill.getMpNeed()) {
                notice = "角色蓝量不够";
            }
        }
        if(targetIdList != null && targetIdList.size() > 1 && !skill.getSkillRange().equals(SkillRange.Enemys)) {
            notice = "该技能不能针对多个敌方";
        }


        if(StringUtils.isEmpty(notice)) {
            return true;
        }

        if(character instanceof Player) {
            Channel channel = playerCache.getChannelByPlayerId(character.getId());
            //进行通知
            channel.writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity(notice));
        }

        return false;
    }

    /**
     * 绑定用户技能
     * @param player
     */
    public void bindSkill(Player player) {
        EntityType entityType = entityService.getType(player.getTypeId());
        for (Skill skill : entityType.getSkillList()) {
            Skill playerSkill = new Skill();
            BeanUtils.copyProperties(skill, playerSkill);
            player.getSkillInUsedMap().put(skill.getId(), playerSkill);
        }
    }

    public Skill getSkill(Integer skillId) {
        return skillCache.get(skillId);
    }
}
