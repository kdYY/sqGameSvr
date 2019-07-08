package org.sq.gameDemo.cli.service;

import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.UserProto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SendOrderService {

    public void register(MsgEntity msgEntity, String[] input) {
        if(input.length >= 2) {
            Map<String,Object> map = splitCmdString(input);
            UserProto.User data = UserProto.User.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setName((String) map.get("name"))
                    .setPassword((String) map.get("password"))
                    .setTime(System.currentTimeMillis())
                    .build();
            msgEntity.setData(data.toByteArray());
        }
    }



    // name=adfa&password=123456
    private Map splitCmdString(String[] input) {
        Map<String, Object> map = new HashMap<>();
        if(input.length >= 2) {
            String[] split = input[1].split("&");
            for (String s : split) {
                String[] split1 = s.split("=");
                map.put(split1[0], split1[1]);
            }
        }
        return map;
    }

    public void login(MsgEntity msgEntity, String[] input) {
        if(input.length >= 2) {
            Map<String,Object> map = splitCmdString(input);
            UserProto.User data = UserProto.User.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setName((String) map.get("name"))
                    .setPassword((String) map.get("password"))
                    .setTime(System.currentTimeMillis())
                    .build();
            msgEntity.setData(data.toByteArray());
        }
    }
}
