package org.sq.gameDemo.svr.common.dispatch;

import java.lang.reflect.Method;

public class OrderBean {
    private String beanName;
    private Method method;

    public OrderBean(String beanName, Method method) {
        this.beanName = beanName;
        this.method = method;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
