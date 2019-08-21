package org.sq.gameDemo.svr.game.characterEntity.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;
import org.sq.gameDemo.svr.game.characterEntity.dao.EntityTypeCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.SenceEntityCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillService;

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class BabyService {

    @Autowired
    private SenceEntityCache senceEntityCache;
    @Autowired
    private SenceService senceService;
    @Autowired
    private SkillService skillService;

    public void bindBaby(Player player) {
        SenceEntity senceEntity = senceEntityCache.get(Long.valueOf(player.getTypeId()));
        Baby baby = getInitedBaby(player, senceEntity, player.getSenceId(), player.getLevel());
        player.setBaby(baby);
    }


    /**
     * 初始化宝宝
     */
    public Baby getInitedBaby(Player player, SenceEntity senceEntity, Integer senceId, Integer level) {
        Baby baby = new Baby();
        BeanUtils.copyProperties(senceEntity, baby);
        baby.setId(ConcurrentSnowFlake.getInstance().nextID());
        baby.setEntityTypeId(senceEntity.getId());
        baby.setSenceId(senceId);
        ConcurrentMap<Integer, Skill> collect = Arrays.stream(baby.getSkillStr().trim().split(","))
                .map(Integer::valueOf)
                .filter(str -> skillService.getSkill(str) != null)
                .map(skillService::getSkill)
                .collect(Collectors.toConcurrentMap(
                        skill -> skill.getId(),
                        skill -> {
                            Skill monsterSkill = new Skill();
                            BeanUtils.copyProperties(skill, monsterSkill);
                            monsterSkill.setHurt(skill.getHurt() * level / 10);
                            return monsterSkill;
                        }
                ));
        baby.setSkillInUsedMap(collect);
        baby.setLevel(level);
        baby.setMaster(player);
        return baby;
    }

}
