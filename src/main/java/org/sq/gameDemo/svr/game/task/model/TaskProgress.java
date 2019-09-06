package org.sq.gameDemo.svr.game.task.model;

import com.google.common.base.Strings;
import lombok.Data;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.game.task.model.config.FinishCondition;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class TaskProgress {
    //每次都要new一哈
    private Task task;

    private Integer taskId;//主键
    /**
     * [{"field":2,"target":11001, "goal":1},
     * {"field":2,"target":21002, "goal":1},
     * {"field":2,"target":31003, "goal":1},
     * {"field":2,"target":71007, "goal":1},
     * {"field":2,"target":81008, "goal":1} ]
     */

    private Integer unId;
    private Integer state;
    private Date beginTime;
    private Date endTime;
    //存放
    private String progress;

    //初始化任务进度
    public void setTask(Task task) {
        this.task = task;
        if(progresseList.size() != 0 && task != null && task.getFinishConditionList().size() != 0) {
            for (FinishCondition finishCondition : task.getFinishConditionList()) {
                progresseList.add(new Progress(finishCondition));
            }
        }
    }

    List<Progress> progresseList = new CopyOnWriteArrayList<>();

    public List<Progress> getProgresseList() {
        if(progresseList.size() == 0 && Strings.isNullOrEmpty(progress) && !progresseList.equals("[]")) {
            progresseList.addAll(JsonUtil.reSerializableJson(progress, Progress.class));
        }
        return progresseList;
    }

    public String getProgress() {
        return JsonUtil.serializableJson(progresseList);
    }
}
