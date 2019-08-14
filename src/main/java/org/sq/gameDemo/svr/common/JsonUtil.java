package org.sq.gameDemo.svr.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

public class JsonUtil {

    public static JSONObject parseObject(String jsonStr) {
        return JSONObject.parseObject(jsonStr);
    }
    public  static List  reSerializableJson(String jsonStr, Class clazz) {
        return JSONArray.parseArray(jsonStr, clazz);
    }
    public static <T>  T  reJson(String jsonStr, Class<T> clazz) {
        return JSONArray.parseObject(jsonStr, clazz);
    }

    public static  <T> T  reSerializableJson(String jsonStr, TypeReference<T> type) {
        return JSONArray.parseObject(jsonStr, type);
    }

    public  static String  serializableJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    public  static String  serializableArray(Object... obj) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : obj) {
            stringBuilder.append(JSON.toJSONString(o) + "\r\n");
        }
        return stringBuilder.toString();
    }

}
