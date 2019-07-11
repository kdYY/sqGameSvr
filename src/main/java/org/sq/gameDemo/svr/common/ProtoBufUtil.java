package org.sq.gameDemo.svr.common;

import java.lang.reflect.Method;
import java.util.List;

public class ProtoBufUtil {

    public <T,K> T transformProto(T goalBuilder, K sourceBean) {
        Method[] declaredMethods = goalBuilder.getClass().getDeclaredMethods();
        //获取K中的所有属性名称，排除@TransferProto(ignore=false)的属性

        //进行K的get方法的获取，Map记录<属性名，get属性()>

        //T的get方法获取，Map记录<K有的属性名， set属性()>

        //进行方法执行

        //return goalBuilder.setId(sence.getId()).setName(sence.getName()).build();
        return null;
    }

    public <T,K> T transformProtoList(T goalBuilder, List<K> sourceBean) {
        Method[] declaredMethods = goalBuilder.getClass().getDeclaredMethods();
        //获取K中的所有属性名称，排除@TransferProto(ignore=false)的属性

        //进行K的get方法的获取，Map记录<属性名，get属性()>

        //T的get方法获取，Map记录<K有的属性名， set属性()>

        //进行方法执行

        //return goalBuilder.setId(sence.getId()).setName(sence.getName()).build();
        return null;
    }
}
