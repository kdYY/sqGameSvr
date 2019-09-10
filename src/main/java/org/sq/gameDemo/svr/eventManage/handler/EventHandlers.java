package org.sq.gameDemo.svr.eventManage.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.*;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.drop.service.DropPool;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.task.model.config.TaskType;
import org.sq.gameDemo.svr.game.task.model.config.condition.FinishField;
import org.sq.gameDemo.svr.game.task.service.TaskService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2019/1/2 15:32
 * @version 1.00
 * Description: 等级事件处理
 */
@Slf4j
@Component
public class EventHandlers {

    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private DropPool dropPool;
    @Autowired
    private SenceService senceService;
    @Autowired
    private BagService bagService;
    @Autowired
    private TaskService taskService;

    {
        EventBus.registe(LevelEvent.class, this::levelUp);
        log.info("角色升级事件注册成功");
        EventBus.registe(MonsterBeAttackedEvent.class, this::monsterBeAttacked);
        log.info("怪物被攻击事件注册成功");
        EventBus.registe(MonsterDeadEvent.class, this::monsterDead);
        log.info("怪物死亡事件注册成功");
        EventBus.registe(PlayerDeadEvent.class, this::playerDead);
        log.info("人物死亡事件注册成功");
        EventBus.registe(ConversationEvent.class, this::conversation);
        log.info("聊天事件注册成功");
        EventBus.registe(CollectorEvent.class, this::collectItem);
    }

    private  void collectItem(CollectorEvent collectorEvent) {
        taskService.checkTaskFinish(collectorEvent.getPlayer(),
                TaskType.COLLECTION,
                FinishField.ITEMINFO_ID,
                collectorEvent.getItem().getItemInfo().getId());
    }


    /**
     * 玩家跟npc对话
     */
    private void conversation(ConversationEvent conversationEvent) {
        taskService.checkTaskFinish(conversationEvent.getPlayer(),
                TaskType.CONVERSATION,
                FinishField.ENTITY_TYPE,
                conversationEvent.getNpc().getTypeId());
    }

    /**
     * 玩家升级事件处理
     * @param levelEvent
     */
    private  void levelUp(LevelEvent levelEvent) {
        Optional.ofNullable(levelEvent.getPlayer()).ifPresent(player -> {
            Integer level = player.getLevel();
            Integer newlevel = levelEvent.getNewlevel();
            player.setLevel(newlevel);

            playerCache.getChannelByPlayerId(player.getId()).writeAndFlush(
                    ProtoBufUtil.getBroadCastDefaultEntity("恭喜你升了"+ (newlevel - level) + "级，目前等级是" + newlevel));
        });
    }


    /**
     * 怪物被攻击事件处理
     * @param attackedEvent
     */
    private  void monsterBeAttacked(MonsterBeAttackedEvent attackedEvent) {
        //怪物被攻击
        Monster targetMonster = attackedEvent.getTargetMonster();
        Character attacter = attackedEvent.getAttacter();

        //再做一次校验
        if(targetMonster != null &&((UserEntity)attacter).getTarget().getId().equals(targetMonster.getId())) {
            targetMonster.setState(CharacterState.ATTACKING.getCode());
            // TODO 调试通放开
            // monsterAIService.monsterAttacking(targetMonster);
        }


    }

    /**
     * 怪物死亡事件处理 注意，不在这设置monster的属性
     * @param monsterDeadEvent
     */
    private  void monsterDead(MonsterDeadEvent monsterDeadEvent) {
        //玩家 检查任务进度，掉落物品，触发幸运大爆 ....
        //掉落物品
        Player attacter = monsterDeadEvent.getAttacter();
        Monster deadMonster = monsterDeadEvent.getTargetMonster();

        List<Item> dropItems = dropPool
                .getDropItems(attacter, deadMonster);

        dropItems.forEach(item -> {
            senceService.notifyPlayerByDefault(attacter, "掉落" + item.getItemInfo().getName()
                    + " * " + item.getCount());
            bagService.addItemInBag((Player) attacter, item);
        });

        //检查任务进度
        taskService.checkTaskFinish(attacter, TaskType.KILLING, FinishField.ENTITY_TYPE, deadMonster.getTypeId());
    }

    /**
     * 玩家被其他玩家打死事件
     * @param playerDeadEvent
     */
    private synchronized void playerDead(PlayerDeadEvent playerDeadEvent) {
        Character attacter = playerDeadEvent.getAttacter();
        Player player = playerDeadEvent.getPlayer();
        Map<Integer, Item> equipmentBar = player.getEquipmentBar();
        if(equipmentBar.size() != 0) {
            Map.Entry<Integer, Item> dropEntry = equipmentBar.entrySet().stream().findAny().get();
            if(equipmentBar.remove(dropEntry.getKey(), dropEntry.getValue())) {
                Item item = dropEntry.getValue();
                senceService.notifyPlayerByDefault(player, "被玩家打死，导致身上掉落装备 " + item.getItemInfo().getName() + " * " + item.getCount());
                senceService.notifyPlayerByDefault(attacter, "你将玩家打死, 从他身上爆出 " + item.getItemInfo().getName() + " * " + item.getCount());
                bagService.addItemInBag((Player) attacter, item);
            }
        }
    }


}
