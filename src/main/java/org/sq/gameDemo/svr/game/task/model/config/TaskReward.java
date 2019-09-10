package org.sq.gameDemo.svr.game.task.model.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务结束获得
 */
@Data
public class TaskReward {


    private List<RewardItem> itemList = new ArrayList<>();
    private List<Integer> nextTask = new ArrayList<>();

    @Data
    public static class RewardItem {
        private Integer itemInfoId;
        private Integer num;
    }

}
