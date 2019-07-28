package org.sq.gameDemo.svr.game.skills.service;

import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.model.SkillRange;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SkillRangeService {

    private Map<SkillRange, ISkillRange>  skillRangeMap = new HashMap<>();

    @PostConstruct
    private void init() {
        skillRangeMap.put(SkillRange.SingleEnemy, this::skillSingleEnemy);
        skillRangeMap.put(SkillRange.Enemys, this::skillEnemys);
        skillRangeMap.put(SkillRange.FriendsAndSelf, this::skillFriendAndSelf);
        skillRangeMap.put(SkillRange.SingleFriendAndSelf, this::skillSingleFriendAndSelf);
        skillRangeMap.put(SkillRange.Self, this::skillSelf);
    }

    private void skillSelf(Character character, Character character1, SenceConfigMsg senceConfigMsg, Skill skill) {

    }


    private void skillSingleFriendAndSelf(Character character, Character character1, SenceConfigMsg senceConfigMsg, Skill skill) {

    }


    private void skillFriendAndSelf(Character character, Character character1, SenceConfigMsg senceConfigMsg, Skill skill) {

    }

    //根据技能范围路由到相应的方法上

    public void routeSkill(Character attacter, Character targeter, Skill skill, SenceConfigMsg senecMsg) {
        Optional.ofNullable(skillRangeMap.get(SkillRange.getSkillRangeByRangeId(skill.getSkillRange()))).
                ifPresent(s -> s.skillEffect(attacter, targeter, senecMsg,skill));
    }


    private void skillSingleEnemy(Character attacter, Character target, SenceConfigMsg senecMsg, Skill skill) {
        attacter.setMp(attacter.getMp() - skill.getMpNeed());
        target.setHp(target.getHp() - skill.getHurt());

        UserCache.broadcastChannelGroupBysenceId(senecMsg.getSenceId(),
                attacter.getName() + "受到单体攻击技能:" + skill.getName() + "攻击，受到" + skill.getHurt() + "伤害，目前hp为" + attacter.getHp());

    }

    private void skillEnemys(Character attacter, Character target, SenceConfigMsg senecMsg, Skill skill) {
        attacter.setMp(attacter.getMp() - skill.getMpNeed());
        target.setHp(target.getHp() - skill.getHurt());

        UserCache.broadcastChannelGroupBysenceId(senecMsg.getSenceId(),
                attacter.getName() + "受到群体攻击技能:" + skill.getName() + "攻击，受到" + skill.getHurt() + "伤害，目前hp为" + attacter.getHp());

    }




}
