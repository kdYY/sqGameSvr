package org.sq.gameDemo.svr.game.characterEntity.model;


import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.task.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Npc类
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Npc extends SenceEntity implements Character {
    @ProtoField(Ignore = true)
    private Integer entityTypeId;

    @ProtoField(Ignore = true)
    public List<Integer> taskList = new ArrayList<>();

    public List<Integer> getTaskList() {
        if(!Strings.isNullOrEmpty(getTaskStr())) {
            taskList.addAll(JsonUtil.reSerializableJson(getTaskStr(), Integer.class));
        }
        return taskList;
    }
}
