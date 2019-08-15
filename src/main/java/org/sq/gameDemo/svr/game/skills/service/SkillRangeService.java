package org.sq.gameDemo.svr.game.skills.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.buff.service.BuffService;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.model.SkillRange;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SkillRangeService {

    @Autowired
    private SenceService senceService;

    private Map<SkillRange, ISkillRange>  skillRangeMap = new HashMap<>();

    @PostConstruct
    private void init() {
        skillRangeMap.put(SkillRange.SingleEnemy, this::skillSingleEnemy);
        skillRangeMap.put(SkillRange.Enemys, this::skillEnemys);
        skillRangeMap.put(SkillRange.FriendsAndSelf, this::skillFriendsAndSelf);
        skillRangeMap.put(SkillRange.SingleFriendAndSelf, this::skillSingleFriendAndSelf);
        skillRangeMap.put(SkillRange.Self, this::skillSelf);
    }

    /**
     * 根据技能范围路由到相应的方法上
     * @param attacter
     * @param targeter
     * @param skill
     * @param senecMsg
     */
    public void routeSkill(Character attacter, Character targeter, Skill skill, SenceConfigMsg senecMsg) {

        Optional.ofNullable(skillRangeMap.get(SkillRange.getSkillRangeByRangeId(skill.getSkillRange())))
                .ifPresent(s -> s.skillEffect(attacter, targeter, senecMsg, skill));
    }

    private void skillSelf(Character attacter, Character target, SenceConfigMsg senecMsg, Skill skill) {
        attacter.setMp(attacter.getMp() - skill.getMpNeed());
        target.setHp(attacter.getHp() + skill.getHeal());



        senceService.notifySenceByDefault(senecMsg.getSenceId(),
                target.getName()
                        + "受到单体治疗技能:"
                        + skill.getName()
                        + "受到"
                        + skill.getHeal()
                        + "治疗，目前hp为"
                        + target.getHp());

    }


    private void skillSingleFriendAndSelf(Character attacter, Character target, SenceConfigMsg senecMsg, Skill skill) {
        attacter.setMp(attacter.getMp() - skill.getMpNeed());
        target.setHp(attacter.getHp() + skill.getHeal());

        senceService.notifySenceByDefault(senecMsg.getSenceId(),
                target.getName()
                        + "受到两方治疗技能:"
                        + skill.getName()
                        + "受到"
                        + skill.getHeal()
                        + "治疗，目前hp为"
                        + target.getHp());


    }


    private void skillFriendsAndSelf(Character attacter, Character target, SenceConfigMsg senecMsg, Skill skill) {
        attacter.setMp(attacter.getMp() - skill.getMpNeed());
        target.setHp(attacter.getHp() + skill.getHeal());

        senceService.notifySenceByDefault(senecMsg.getSenceId(),
                target.getName()
                        + "受到群体治疗技能:"
                        + skill.getName()
                        + "受到"
                        + skill.getHeal()
                        + "治疗，目前hp为"
                        + target.getHp());

    }

    private void skillSingleEnemy(Character attacter, Character target, SenceConfigMsg senecMsg, Skill skill) {
        attacter.setMp(attacter.getMp() - skill.getMpNeed());
        target.setHp(target.getHp() - skill.getHurt());

        senceService.notifySenceByDefault(senecMsg.getSenceId(),
                target.getName()
                        + "受到单体攻击技能:"
                        + skill.getName()
                        + "攻击，受到"
                        + skill.getHurt()
                        + "伤害，目前hp为"
                        + target.getHp());

    }

    private void skillEnemys(Character attacter, Character target, SenceConfigMsg senecMsg, Skill skill) {
        attacter.setMp(attacter.getMp() - skill.getMpNeed());
        target.setHp(target.getHp() - skill.getHurt());

        senceService.notifySenceByDefault(senecMsg.getSenceId(),
                target.getName()
                        + "受到群体攻击技能:"
                        + skill.getName()
                        + "攻击，受到"
                        + skill.getHurt()
                        + "伤害，目前hp为"
                        + target.getHp());

    }

}
