package org.sq.gameDemo.svr.game.task.model;

import com.google.common.base.Strings;
import lombok.Data;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.poiUtil.ExcelFeild;
import org.sq.gameDemo.svr.game.task.model.config.FinishCondition;
import org.sq.gameDemo.svr.game.task.model.config.TaskReward;

import java.util.ArrayList;
import java.util.List;

@Data
public class Task {
    Integer id;
    String name;
    Integer type;
    Integer kind;
    String finishStr;
    String rewardItemStr;
    String description;

    @ExcelFeild(Ignore = true)
    private List<FinishCondition> finishConditionList = new ArrayList<>();


    public List<FinishCondition> getFinishConditionList() {
        if(finishConditionList.size() == 0 && Strings.isNullOrEmpty(finishStr)) {
            finishConditionList.addAll(JsonUtil.reSerializableJson(finishStr, FinishCondition.class));
        }
        return finishConditionList;
    }

    @ExcelFeild(Ignore = true)
    private List<TaskReward> taskRewardList = new ArrayList<>();


    public List<TaskReward> getTaskRewardList() {
        if(taskRewardList.size() == 0 && Strings.isNullOrEmpty(rewardItemStr)) {
            taskRewardList.addAll(JsonUtil.reSerializableJson(rewardItemStr, TaskReward.class));
        }
        return taskRewardList;
    }
}
