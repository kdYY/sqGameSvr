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
import java.util.function.Consumer;
import java.util.function.Function;
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

    /**
     * 初始化日常任务
     * @param taskProgress
     */
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
     * 检测任务进度， 任务是否完成 用于事件检查
     */
    public void checkTaskProgress(Player player, TaskType taskType, FinishField finishField, Integer target, Consumer<Progress> consumer) {

        List<TaskProgress> taskProgressList = player.getTaskProgressMap().values()
                .stream()
                .filter(tp -> tp.getTask().getType().equals(taskType.getTypeId()) && tp.getState().equals(TaskStateConstant.DOING))
                .filter(tp -> tp.getTask().getFinishConditionList().stream().anyMatch(f -> f.getField().equals(finishField.getField())))
                .collect(Collectors.toList());

        for (TaskProgress taskProgress : taskProgressList) {
            //根据field进行条件的判断
            taskProgress.getProgresseList().stream()
                    .filter(tp -> tp.getCondition().getTarget().equals(target))
                    .forEach(consumer);

            if(taskProgress.getProgresseList().stream().filter(p -> !p.isFinished()).findFirst().isPresent()) {
                //任务完成
                finishTask(player, taskProgress);
            }
        }

    }

    /**
     * 更新数据库中的任务进度
     * @param taskProgress
     */
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
            updateTaskProgress(taskProgress);
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
            for (Integer taskId : nextTask) {
                //初始化taskprogress
                addAcceptTask(player, taskId);
            }
        }
    }

    //增加任务
    private void addAcceptTask(Player player, Integer taskId) {
        if(player.getTaskProgressMap().get(taskId) != null) {
            return;
        }
        Task task = taskCache.get(taskId);
        TaskProgress taskProgress = new TaskProgress(player, task);
        for (FinishCondition finishCondition : taskProgress.getTask().getFinishConditionList()) {
            taskProgress.getProgresseList().add(new Progress(finishCondition));
        }
        insert(taskProgress);
        player.getTaskProgressMap().put(taskId, taskProgress);
        senceService.notifyPlayerByDefault(player, "解锁新任务:" + task.getName() + ", 使用showTaskCanAccpet");
    }

    /**
     * 插入，返回主键
     * @param taskProgress
     */
    private void insert(TaskProgress taskProgress) {
        if(taskProgressMapper.insertSelective(taskProgress) <= 0) {
            log.info("progress插入失败");
        }
    }




    /**
     * 放弃任务
     */












}