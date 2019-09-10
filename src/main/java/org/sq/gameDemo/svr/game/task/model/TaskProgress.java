package org.sq.gameDemo.svr.game.task.model;

import com.google.common.base.Strings;
import lombok.Data;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class TaskProgress {

    private Integer id;//主键
    private Integer taskId;
    private Integer unId;
    private Integer state;
    private Long beginTime;
    private Long endTime;
    //存放
    @ProtoField(Ignore = true)
    private String progress;

    public TaskProgress() {}

    private Task task;

    public TaskProgress(Player player, Task task) {
        this.unId = player.getUnId();
        this.beginTime = System.currentTimeMillis();
        state = TaskStateConstant.CAN_ACCEPT;
        taskId = task.getId();
        this.task = task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    List<Progress> progresseList = new CopyOnWriteArrayList<>();

    public List<Progress> getProgresseList() {
        if(progresseList.size() == 0 && Strings.isNullOrEmpty(progress) && !progress.equals("[]")) {
            progresseList.addAll(JsonUtil.reSerializableJson(progress, Progress.class));
        }
        return progresseList;
    }

    public String getProgress() {
        return JsonUtil.serializableJson(progresseList);
    }
}
