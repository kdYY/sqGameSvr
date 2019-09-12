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
import org.sq.gameDemo.svr.game.drop.service.DropPool;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.task.model.config.TaskType;
import org.sq.gameDemo.svr.game.task.model.config.condition.FinishField;
import org.sq.gameDemo.svr.game.task.model.config.condition.FirstTarget;
import org.sq.gameDemo.svr.game.task.model.config.condition.LevelTarget;
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
        EventBus.registe(MonsterDeadEvent.class, this::monsterDead);
        log.info("怪物死亡事件注册成功");
        EventBus.registe(PlayerPKEvent.class, this::playerDead);
        log.info("人物PK事件注册成功");
        EventBus.registe(ConversationEvent.class, this::conversation);
        log.info("聊天事件注册成功");
        EventBus.registe(CollectorEvent.class, this::collectItem);
        log.info("物品收集事件注册成功");
        EventBus.registe(WearEquipEvent.class, this::equipCheck);
        log.info("穿戴装备事件注册成功");
        EventBus.registe(PlayerAddGuildEvent.class, this::addGuild);
        log.info("加入公会事件注册成功");
        EventBus.registe(FirstTradeEvent.class, this::makeTrade);
        log.info("参与交易事件注册成功");
        EventBus.registe(TaskFinishedEvent.class, this::taskFinish);
        log.info("参与任务事件注册成功");
        EventBus.registe(ActivatTaskEvent.class, this::activatTask);
        log.info("激活任务事件注册成功");

    }

    /**
     * 激活任务
     * @param activatTaskEvent
     */
    private void activatTask(ActivatTaskEvent activatTaskEvent) {
        for (Integer id : activatTaskEvent.getTaskId()) {
            taskService.addAcceptTask(activatTaskEvent.getPlayer(), id);
        }
    }

    /**
     * 任务完成
     * @param taskFinishedEvent
     */
    private  void taskFinish(TaskFinishedEvent taskFinishedEvent) {
        taskService.finishTask(taskFinishedEvent.getPlayer(), taskFinishedEvent.getTaskProgress());
    }

    /**
     * 首次完成交易
     * @param firstTradeEvent
     */
    private void makeTrade(FirstTradeEvent firstTradeEvent) {
        firstTaskEvent(firstTradeEvent.getPlayer(), FirstTarget.FINISH_TRADE);
    }

    /**
     * 加入公会事件
     */
    private void addGuild(PlayerAddGuildEvent guildEvent) {
        firstTaskEvent(guildEvent.getPlayer(), FirstTarget.ADD_GUILD);
    }



    /**
     * 玩家被其他玩家打死事件
     * @param playerPKEvent
     */
    private synchronized void playerDead(PlayerPKEvent playerPKEvent) {
        Character attacter = playerPKEvent.getAttacter();
        Player player = playerPKEvent.getPlayer();
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
        firstTaskEvent((Player) attacter, FirstTarget.FINISH_PK);
    }


    /**
     * 首次事件处理
     * @param player
     * @param firstTarget
     */
    private void firstTaskEvent(Player player, FirstTarget firstTarget) {
        taskService.checkTaskProgress(player,
                TaskType.FIRST,
                FinishField.FIRST,
                firstTarget.getCode(),
                progress -> progress.addProgressNum(1));
    }

    /**
     * 收集物品
     */
    private  void collectItem(CollectorEvent collectorEvent) {
        taskService.checkTaskProgress(collectorEvent.getPlayer(),
                TaskType.COLLECTION,
                FinishField.ITEMINFO_ID,
                collectorEvent.getItem().getItemInfo().getId(),
                progress -> progress.addProgressNum(collectorEvent.getItem().getCount()));
    }


    /**
     * 玩家跟npc对话
     */
    private void conversation(ConversationEvent conversationEvent) {
        taskService.checkTaskProgress(conversationEvent.getPlayer(),
                TaskType.CONVERSATION,
                FinishField.ENTITY_TYPE,
                conversationEvent.getNpc().getTypeId(),
                progress -> progress.addProgressNum(1));
    }

    /**
     * 玩家升级事件处理
     */
    private  void levelUp(LevelEvent levelEvent) {
        Optional.ofNullable(levelEvent.getPlayer()).ifPresent(player -> {
            Integer level = player.getLevel();
            Integer newlevel = levelEvent.getNewlevel();
            player.setLevel(newlevel);

            playerCache.getChannelByPlayerId(player.getId()).writeAndFlush(
                    ProtoBufUtil.getBroadCastDefaultEntity("恭喜你升了"+ (newlevel - level) + "级，目前等级是" + newlevel));

            taskService.checkTaskProgress(player,
                    TaskType.FUNCTIONAL_UPGRADE,
                    FinishField.PLAYER_LEVEL,
                    LevelTarget.PLAYER.getCode(),
                    progress -> progress.setProgressNumber(newlevel));
        });
    }

    /**
     * 装备更换事件
     */
    private void equipCheck(WearEquipEvent wearEquipEvent) {
        Player player = wearEquipEvent.getPlayer();
        Long count = player.getEquipmentBar().values().stream().map(equip -> equip.getLevel()).count();

        taskService.checkTaskProgress(player,
                TaskType.FUNCTIONAL_UPGRADE,
                FinishField.ALL_EQUIP_LEVEL,
                LevelTarget.EQUIPALL.getCode(),
                progress -> progress.setProgressNumber(count.intValue()));
    }



    /**
     * 怪物死亡事件处理 注意，不在这设置monster的属性
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
        taskService.checkTaskProgress(attacter,
                TaskType.KILLING,
                FinishField.ENTITY_TYPE,
                deadMonster.getEntityTypeId().intValue(),
                progress -> progress.addProgressNum(1));
    }


}
