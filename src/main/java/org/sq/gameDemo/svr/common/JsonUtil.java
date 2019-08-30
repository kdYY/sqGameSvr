package org.sq.gameDemo.svr.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取map
     */
    public static  <T extends Map> T getMap(T t, String str)  {
        if(t.size() == 0 && !StringUtils.isEmpty(str)) {
            t = reSerializableJson(str, new TypeReference<T>(){});
        }
        return t;
    }

    /**
     * 获取jsonStr
     */
    public static  <T extends Map> String getJsonStr(T t, String str)  {
        if(t.size() != 0 && StringUtils.isEmpty(str)) {
           str = serializableJson(t);
        }
        return str;
    }

}
