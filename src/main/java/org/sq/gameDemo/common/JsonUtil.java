package org.sq.gameDemo.common;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

    public static JSONObject parseObject(String jsonStr) {
        return JSONObject.parseObject(jsonStr);
    }
}
