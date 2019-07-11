package org.sq.gameDemo.svr.game.scene.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class  SenceConfigMsg {
    private int senceId;

    @JSONField(serialize = false)
    private String jsonStr;

    private List<SenceEntity> senceEntities = new ArrayList<>();
    private List<UserEntity> userEntities = new ArrayList<>();
}
