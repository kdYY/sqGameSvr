package org.sq.gameDemo.svr.game.scene.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.common.proto.SenceEntityProto;
import org.sq.gameDemo.common.proto.UserEntityProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.characterEntity.model.EntityType;
import org.sq.gameDemo.svr.game.characterEntity.model.SenceEntity;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;

import java.util.List;

/**
 * 场景配置读取类
 */
@Data
public class SenceConfig {
    private int senceId;

    private String jsonStr;

    @Data
    public static class tmpConf {
        private int id;
        private int num;
    }
}
