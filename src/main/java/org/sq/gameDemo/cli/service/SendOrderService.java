package org.sq.gameDemo.cli.service;

import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.UserProto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SendOrderService {

    /**
     * 注册
     * @param msgEntity
     * @param input
     */
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

    /**
     * 登陆
     * @param msgEntity
     * @param input
     */
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


    /**
     * 获取指令帮助
     */
    public void help() {
        System.out.println(
                "注册: register name=test&password=123456\r\n"
                + "登陆: login name=test&password=123456\r\n"
                        + "查看可创建角色: getRoleMsg\r\n"
                + "绑定角色: bindRole name=剑圣\r\n"
        );
    }

    /**
     * 绑定角色
     * @param msgEntity
     * @param input
     */
    public void bindRole(MsgEntity msgEntity, String[] input) {


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
}
