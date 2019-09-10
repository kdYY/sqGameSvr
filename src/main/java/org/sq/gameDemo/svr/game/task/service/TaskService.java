package org.sq.gameDemo.svr.game.task.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.task.dao.TaskCache;
import org.sq.gameDemo.svr.game.task.dao.TaskProgressMapper;
import org.sq.gameDemo.svr.game.task.model.*;
import org.sq.gameDemo.svr.game.task.model.config.FinishCondition;
import org.sq.gameDemo.svr.game.task.model.config.TaskKind;
import org.sq.gameDemo.svr.game.task.model.config.TaskReward;
import org.sq.gameDemo.svr.game.task.model.config.TaskType;
import org.sq.gameDemo.svr.game.task.model.config.condition.FinishField;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {


    @Autowired
    private TaskCache taskCache;
    @Autowired
    private TaskProgressMapper taskProgressMapper;
    @Autowired
    private SenceService senceService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private BagService bagService;


    /**
     * load任务进度到玩家实体
     */
    public void loadTaskProgress(Player player) {
        TaskProgressExample taskProgressExample = new TaskProgressExample();
        taskProgressExample.createCriteria().andUnIdEqualTo(player.getUnId()).andStateNotEqualTo(TaskStateConstant.FINISH);
        List<TaskProgress> taskProgresses = taskProgressMapper.selectByExample(taskProgressExample);

        for (TaskProgress taskProgress : taskProgresses) {
            Task task = taskCache.get(taskProgress.getTaskId());
            taskProgress.setTask(task);
            //初始化日常任務
            if(task.getKind().equals(TaskKind.DAILY.getKind())) {
                initDailyTask(taskProgress);
            }
            player.getTaskProgressMap().put(taskProgress.getTaskId(), taskProgress);
        }
        log.info("玩家任务进度加载完毕");
    }

    private void initDailyTask(TaskProgress taskProgress) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(taskProgress.getBeginTime());
        int beginDay = instance.get(Calendar.DAY_OF_MONTH);
        instance.setTimeInMillis(System.currentTimeMillis());
        int nowDay = instance.get(Calendar.DAY_OF_MONTH);
        if(beginDay < nowDay) {
            taskProgress.setProgress("");
            List<Progress> progresseList = taskProgress.getProgresseList();
            progresseList.clear();
            //日常任务
            for (FinishCondition finishCondition : taskProgress.getTask().getFinishConditionList()) {
                progresseList.add(new Progress(finishCondition));
            }
            taskProgress.setBeginTime(System.currentTimeMillis());
            updateTaskProgress(taskProgress);
        }

    }


    /**
     * 保存，创建任务
     */







    /**
     * 某个任务中某一项进度+1
     */
    public void doTaskProgress(TaskProgress taskProgress, Integer field, Integer target) {
        taskProgress.getProgresseList().stream()
                .filter(t -> t.getCondition().getField().equals(field) && t.getCondition().getTarget().equals(target))
                .findFirst()
                .ifPresent(t -> {
                    int result = t.getProgressNum().incrementAndGet();
                    if(t.getCondition().getGoal() <= result) {
                        //任务中一项完成
                        t.setFinished(true);
                    }
                });
    }


    /**
     * 检测任务进度， 任务是否完成 用于事件检查
     */
    public void checkTaskFinish(Player player, TaskType taskType, FinishField finishField, Integer target) {
        List<TaskProgress> taskProgressList = player.getTaskProgressMap().values()
                .stream()
                .filter(tp -> tp.getTask().getType().equals(taskType.getTypeId()))
                .filter(tp -> tp.getTask().getFinishConditionList().stream().anyMatch(f -> f.getField().equals(finishField.getField())))
                .collect(Collectors.toList());

        for (TaskProgress taskProgress : taskProgressList) {
            //根据field进行条件的判断
            if(taskProgress.getState().equals(TaskStateConstant.DOING)) {
                taskProgress.getProgresseList().stream()
                        .filter(tp -> tp.getCondition().getTarget().equals(target))
                        .forEach(tp -> tp.getProgressNum().incrementAndGet());

                if(taskProgress.getProgresseList().stream().filter(p -> !p.isFinished()).findFirst().isPresent()) {
                    //任务完成
                    finishTask(player, taskProgress);
                }
            }
        }
    }

    private void updateTaskProgress(TaskProgress taskProgress) {
        ThreadManager.dbTaskPool.execute(() -> taskProgressMapper.updateByPrimaryKey(taskProgress));
    }

    /**
     * 使用showTaskCanAccpet查看当前可领取任务
     */
    public List<Task> showTaskCanAccept(Player player) {
        return player.getTaskProgressMap().values()
                .stream()
                .filter(t -> t.getTask().getLevel() <= player.getLevel())
                .filter(t -> t.getState().equals(TaskStateConstant.CAN_ACCEPT))
                .map(t -> t.getTask())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 使用showTask查看当前正在执行的任务以及进度
     */
    public List<TaskProgress> showTaskProgress(Player player) {
        return player.getTaskProgressMap().values()
                .stream()
                .filter(t -> t.getState().equals(TaskStateConstant.DOING))
                .collect(Collectors.toList());
    }


    /**
     * 接受任务
     */
    public void acceptTask(Player player, Integer taskId) {
        Task task = taskCache.get(taskId);
        if(Objects.isNull(task)) {
            senceService.notifyPlayerByDefault(player, "无此任务，使用showTaskCanAccpet查看当前可领取任务");
            return;
        }
        TaskProgress taskProgress = player.getTaskProgressMap().get(task.getId());
        if(taskProgress != null) {
            taskProgress.setState(TaskStateConstant.DOING);
            senceService.notifyPlayerByDefault(player, "接受新任务:(id=" + task.getId() + ", name=" + task.getName() + ") 使用showTask查看当前任务吧!");
        } else {
            senceService.notifyPlayerByDefault(player, "任务尚未激活，使用showTaskCanAccpet查看当前可领取任务");
        }
    }


    /**
     * 任务发放奖励 触发下一个任务
     */
    public void finishTask(Player player, TaskProgress taskProgress) {
        taskProgress.setState(TaskStateConstant.FINISH);
        updateTaskProgress(taskProgress);
        TaskReward taskReward = taskProgress.getTask().getTaskReward();
        List<TaskReward.RewardItem> itemList = taskReward.getItemList();
        for (TaskReward.RewardItem rewardItem : itemList) {
            ItemInfo itemInfo = bagService.getItemInfo(rewardItem.getItemInfoId());
            Item item = bagService.createItem(itemInfo, rewardItem.getNum(), itemInfo.getLevel());
            bagService.addItemInBag(player, item);
        }
        List<Integer> nextTask = taskReward.getNextTask();
        if(nextTask.size() != 0) {
            senceService.notifyPlayerByDefault(player, "新任务已解锁");
            for (Integer taskId : nextTask) {
                acceptTask(player, taskId);
            }
        }
    }


    /**
     * 放弃任务
     */












}
