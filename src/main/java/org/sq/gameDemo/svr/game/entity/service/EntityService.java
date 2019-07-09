package org.sq.gameDemo.svr.game.entity.service;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.entity.model.Entity;
import org.sq.gameDemo.svr.game.scene.model.SenceData;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EntityService {


    @Autowired
    private UserService userService;
    @Autowired
    private SenceService senceService;

    @Value("${excel.entity}")
    private String entityFileName;

    //存储所有的实体信息
    private List<Entity> entities;

    @PostConstruct
    public void initalEntity() {
        try {
            entities = PoiUtil.readExcel(entityFileName, 0, Entity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public String getEntitieListString() {
        StringBuilder builder = new StringBuilder();
        for (Entity entity : entities) {
            builder.append(entity.toString() + "\r\n");
        }
        return builder.toString();
    }


    @OrderMapping(OrderEnum.BindRole)
    public MsgEntity bindRole(MsgEntity msgEntity) throws Exception {
        byte[] data = msgEntity.getData();
        EntityProto.Entity entity = EntityProto.Entity.parseFrom(data);
        int entityId = entity.getId();
        Integer userId = UserCache.getUserIdByChannel(msgEntity.getChannel());

        User userCached = UserCache.getUserById(userId);
        if(userCached == null) {
            User user = userService.getUser(userId);
            userService.updateUser(userId, entityId);

            user.setEntityid(entityId);
            UserCache.addUserMap(userId, user);
        } else {
            userCached.setEntityid(entityId);
        }
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(entity.getMsgId());
        builder.setTime(entity.getTime());
        //场景，场景中的角色信息
        //senceService.getSenecDataById();
        SenceData senecDataById = senceService.getSenecDataById(userCached.getId());
        builder.setContent("");
        return msgEntity;
    }
}
