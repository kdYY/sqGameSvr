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
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.model.SkillRange;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
     * 场景单位使用技能打另一个场景单位
     * @param attacter
     * @param targeter
     * @param skill
     * @param senecMsg
     */
    public boolean characterUseSkillAttack(Character attacter, Character targeter, Skill skill, SenceConfigMsg senecMsg) {


        if(targeter instanceof Npc) {
            if(attacter instanceof UserEntity ) {
                Channel channel = playerCache.getChannelByPlayerId(attacter.getId());
                channel.writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity("npc不能被砍..."));
                return false;
            }
            return false;

        }

        String content = attacter.getName() + "(id=" + attacter.getId() + ")开始使用技能"
                + skill.getName() + "攻击" + targeter.getName()
                + "(id=" + targeter.getId() + ")";

        log.debug(content);

        //开启cd
        makeSkillInCD(attacter, skill);
        //技能若是有释放时间，则延迟释放

        if(attacter instanceof UserEntity && skill.getCastTime() > 0) {
            UserCache.broadcastChannelGroupBysenceId(senecMsg.getSenceId(),
                    attacter.getName() + "开始释放技能，需要" + skill.getCastTime()/1000 + "秒");
        }

        //单线程执行
        TimedTaskManager.singleThreadSchedule(skill.getCastTime() <= 0 ? 0: skill.getCastTime() ,
                ()->{
                    senecMsg.getSingleThreadSchedule().execute(
                            () -> {
                                if(targeter.getHp() > 0) { //??
                                    if(attacter instanceof Player) {
                                        playerCache.getChannelByPlayerId(attacter.getId()).writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity(content));
                                    } else {
                                        playerCache.getChannelByPlayerId(targeter.getId()).writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity(content));
                                    }
                                    skillRangeService.routeSkill(attacter, targeter, skill, senecMsg);
                                }
                            }
                    );
                });
        return true;

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
