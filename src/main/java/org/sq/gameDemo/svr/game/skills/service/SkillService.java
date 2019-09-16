package org.sq.gameDemo.svr.game.skills.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.game.buff.model.Buff;
import org.sq.gameDemo.svr.game.buff.model.BuffState;
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.buff.service.BuffService;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.fight.FightService;
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
    private EntityService entityService;
    @Autowired
    private MonsterAIService monsterAIService;
    @Autowired
    private SkillRangeService skillRangeService;
    @Autowired
    private BuffService buffService;
    @Autowired
    private SkillCache skillCache;
    @Autowired
    private FightService fightService;

    /**
     * 场景单位使用技能打另一个场景单位
     */
    public boolean characterUseSkillAttack(Character attacter, Character targeter, Skill skill, SenceConfigMsg senecMsg) {
        String content = attacter.getName() + "(id=" + attacter.getId() + ")开始使用"
                + skill.getName() + " 作用于 " + targeter.getName()
                + " (id=" + targeter.getId() + ")";
        //开启cd
        makeSkillCD(attacter, skill);
        //技能若是有释放时间，则延迟释放
        if(attacter instanceof UserEntity && skill.getCastTime() > 0) {
            senceService.notifySenceByDefault(senecMsg.getSenceId(),
                    attacter.getName() + "开始释放技能，需要" + skill.getCastTime()/1000 + "秒");
        }
        //System.out.println();
        if(skill.getCastTime() <= 0) {
            senceService.notifyPlayerByDefault(attacter, content);
            skillEffect(attacter, targeter, skill, senecMsg);
        } else {
            //单线程执行 保证任务顺序且不出现某些线程安全问题
            Future future = ThreadManager.singleThreadSchedule(skill.getCastTime() <= 0 ? 0 : skill.getCastTime(),
                    () -> {
                        senecMsg.getSingleThreadSchedule().execute(
                                () -> {
                                    //被怪物杀死后，瞬间复活，然后回到其他场景，此时上次的攻击开始作用，发现hp>0 继续把你砍死， 所以应该判断是否在同一场景
                                    attacter.getSkillInEffectingMap().remove(skill);
                                    senceService.notifyPlayerByDefault(attacter, content);
                                    skillEffect(attacter, targeter, skill, senecMsg);
                                }
                        );
                    });
            attacter.getSkillInEffectingMap().put(skill, future);
        }
        return true;

    }

    /**
     * 对多个目标实施技能
     */
    public boolean characterUseSkillAttackManyTarget(Character attacter, List<Character> targeterList, Skill skill, SenceConfigMsg
            senecMsg) {
        //通知
        String content = attackNotify(attacter, targeterList, skill);
        //开启cd
        makeSkillCD(attacter, skill);
        //技能若是有释放时间，则延迟释放
        if(attacter instanceof UserEntity && skill.getCastTime() > 0) {
            senceService.notifySenceByDefault(senecMsg.getSenceId(),
                    attacter.getName() + "开始释放技能，需要" + skill.getCastTime()/1000 + "秒");
        }

        if(skill.getCastTime() <= 0) {
            senceService.notifyPlayerByDefault(attacter, content);
            targeterList.forEach(targeter -> skillEffect(attacter, targeter, skill, senecMsg));
        } else {
            //单线程执行 保证任务顺序且不出现某些线程安全问题
            Future future = ThreadManager.singleThreadSchedule(skill.getCastTime() <= 0 ? 0 : skill.getCastTime(),
                    () -> {
                        senecMsg.getSingleThreadSchedule().execute(
                                () -> {
                                    //被怪物杀死后，瞬间复活，然后回到其他场景，此时上次的攻击开始作用，发现hp>0 继续把你砍死， 所以应该判断是否在同一场景
                                    attacter.getSkillInEffectingMap().remove(skill);
                                    senceService.notifyPlayerByDefault(attacter, content);
                                    targeterList.forEach(targeter -> skillEffect(attacter, targeter, skill, senecMsg));
                                }
                        );
                    });
            attacter.getSkillInEffectingMap().put(skill, future);
        }
        return true;

    }

    /**
     * 攻击通知内容
     */
    private String attackNotify(Character attacter, List<Character> targeterList, Skill skill) {
        String word = attacter.getName() + "(id=" + attacter.getId() + ")开始使用"
                + skill.getName() + " 作用于 ";
        StringBuffer contentStr = new StringBuffer(word);
        targeterList.forEach(targeter -> {
            contentStr.append(targeter.getName());
            contentStr.append(" (id=");
            contentStr.append(targeter.getId());
            contentStr.append(")");
        });
        return contentStr.toString();
    }

    /**
     * 技能作用效果
     */
    private void skillEffect(Character attacter, Character targeter, Skill skill, SenceConfigMsg senecMsg) {
        if (targeter.getHp() > 0 && targeter.getSenceId().equals(attacter.getSenceId())) {

            skillRangeService.routeSkill(attacter, targeter, skill, senecMsg);

            //记录伤害
            if(senecMsg instanceof CopyScene) {
                Long damage = skill.getHurt();
                ((CopyScene) senecMsg).updateDamage(attacter, damage);
            }

            if (skill.getBuff() != null && skill.getBuff() != 0) {
                Optional.ofNullable(skill.getBuff()).ifPresent(
                        buffId -> {
                            Buff buff = buffService.getBuff(buffId);
                            buff.setCharacter(attacter);
                            buffService.buffAffecting(targeter, buff);
                        }
                );
            }

            if (targeter instanceof Monster) {
                monsterAIService.monsterBeAttacked(attacter, (Monster) targeter);
            }

            if (targeter instanceof Player) {
                fightService.playerBeAttacked(attacter, (Player) targeter);
            }
        }
    }


    /**
         * 使技能进入cd
         */
    private void makeSkillCD(Character attacter, Skill skill) {
        //设置技能使用时间
        skill.setLastUseTime(System.currentTimeMillis());
    }


    /**
     * 判断技能是否能使用
     */
    public boolean skillCanUse(Character character, Skill skill, List<Long> targetIdList) {
        String notice = "";

        if(Objects.isNull(skill)) {
            notice = "技能不存在，技能id有误, 使用showPlayer查看自身技能";
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
        if(!StringUtils.isEmpty(notice) && character instanceof Player) {
            senceService.notifyPlayerByDefault(character, notice);
            return false;
        }
        if(targetIdList != null && targetIdList.size() > 1 && !skill.getSkillRange().equals(SkillRange.Enemys)) {
            notice = "该技能不能针对多个敌方";
        }

        if(character.getSkillInEffectingMap().get(skill) != null) {
            notice = "技能正在延迟释放中";
        }

        if(character.getBufferList().stream().filter(buff -> buff.getId().equals(BuffState.DAZE.getEffectState())).findFirst().isPresent()) {
            notice = "眩晕状态，无法使用技能";
        }

        if(StringUtils.isEmpty(notice)) {
            return true;
        }

        if(character instanceof Player) {
            senceService.notifyPlayerByDefault(character, notice);
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

            skill.setHurt(skill.getHurt() * player.getLevel());
            skill.setHeal(skill.getHeal() * player.getLevel());


            player.getSkillInUsedMap().put(skill.getId(), playerSkill);
        }
    }

    public Skill getSkill(Integer skillId) {
        return skillCache.get(skillId);
    }
}
