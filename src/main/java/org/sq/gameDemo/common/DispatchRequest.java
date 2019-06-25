package org.sq.gameDemo.common;

import java.util.HashMap;
import java.util.Map;

//做请求转发
public class DispatchRequest {

    private static DispatchRequest dispatchRequest = new DispatchRequest(); // 做一个单例的管理

    private Map<String, Class> request2Handler = new HashMap();

    public void init() {
        //做指令初始化工作 eg 读取指令文本文件抑或其他途径

    }

    private DispatchRequest() {
        init();
    }

    public static DispatchRequest getInstance() {
        return dispatchRequest;
    }

    public Class getHandle(String order) {
        return request2Handler.get(order);
    }


}

