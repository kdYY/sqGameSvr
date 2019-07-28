package org.sq.gameDemo.svr.game.skills.service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.sq.gameDemo.svr.common.TimedTaskManager;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.dao.EntityTypeCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.EntityType;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.model.SkillRange;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SkillService {

    @Autowired
    private SkillCache skillCache;
    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private EntityTypeCache typeCache;

    @Autowired
    private SkillRangeService skillRangeService;


    /**
     * 场景单位之间相互砍
     * @param attacter
     * @param targeter
     * @param skill
     * @param senecMsg
     */
    public void characterUseSkillAttack(Character attacter, Character targeter, Skill skill, SenceConfigMsg senecMsg) {
        String content = "场景id" + senecMsg.getSenceId() + ": " + attacter.getName() + "开始使用技能" + skill.getName();
        log.debug(content);

        //如何attacter是boss，开启首刀仇恨
        targeterBelong(attacter.getId(), targeter);
        //开启cd
        makeSkillInCD(attacter, skill);
        //技能释放时间，延迟释放
        if(skill.getCastTime() > 0) {

        } else {
            skillRangeService.routeSkill(attacter, targeter, skill, senecMsg);
            //通知
            UserCache.broadcastChannelGroupBysenceId(senecMsg.getSenceId(), content);

        }

    }

    /**
     * 使用技能前使技能进入cd
     * @param attacter
     * @param skill
     */
    private void makeSkillInCD(Character attacter, Skill skill) {
        Skill usedSkill = new Skill();
        BeanUtils.copyProperties(skill, usedSkill, "description");
        usedSkill.setGrade(skill.getGrade());

        attacter.getSkillInUsedMap().put(skill.getId(), usedSkill);
        // 定时解除cd状态
        TimedTaskManager.schedule(skill.getCd(), () -> attacter.getSkillInUsedMap().remove(skill.getId()) );
    }


    //标记boss归属者
    private void targeterBelong(Long id, Character targeter) {

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
        } else if(skill.isInCD()) {
            notice = "技能正在冷却";
        } else if(character instanceof Player) {
            EntityType type = typeCache.get(((Player) character).getTypeId());
            if(Objects.isNull(type.getSkillMap().get(skill.getId()))) {
                notice = "该角色对应的职业不具备此技能";
            }
            if(character.getMp() < skill.getMpNeed()) {
                notice = "角色蓝量不够";
            }
        }
        if(targetIdList.size() > 1 && !skill.getSkillRange().equals(SkillRange.Enemys)) {
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




}
