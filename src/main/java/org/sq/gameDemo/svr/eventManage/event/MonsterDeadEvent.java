package org.sq.gameDemo.svr.eventManage.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.skills.model.Skill;

@Data
@AllArgsConstructor
public class MonsterDeadEvent extends Event {

    private Player attacter;
    private  Monster targetMonster;

}
