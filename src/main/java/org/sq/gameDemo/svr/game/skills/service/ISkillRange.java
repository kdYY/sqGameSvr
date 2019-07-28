package org.sq.gameDemo.svr.game.skills.service;

import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.skills.model.Skill;

@FunctionalInterface
public interface ISkillRange {
    /**
     * 施放技能造成影响
     * @param attacter
     * @param target
     * @param senecMsg
     * @param skill
     */
    void skillEffect(Character attacter, Character target, SenceConfigMsg senecMsg, Skill skill);
}
