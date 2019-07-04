package org.sq.gameDemo.common;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.MessageLite;
import org.sq.gameDemo.svr.game.entity.model.MessageProto;
import org.sq.gameDemo.svr.game.entity.model.MessageProto2;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    public static JSONObject parseObject(String jsonStr) {
        return JSONObject.parseObject(jsonStr);
    }


}
