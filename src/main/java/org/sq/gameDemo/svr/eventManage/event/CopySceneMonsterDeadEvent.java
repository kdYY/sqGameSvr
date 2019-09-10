package org.sq.gameDemo.svr.eventManage.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
//副本怪物死亡类
@Data
@AllArgsConstructor
public class CopySceneMonsterDeadEvent extends Event {
    CopyScene sence;
    Monster targetMonster;
}
