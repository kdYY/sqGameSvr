package org.sq.gameDemo.common;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//做请求转发
@Component
public class DispatchRequest{

    private ExecutorService executorService = Executors.newFixedThreadPool(NettyConstant.getMaxThreads());
    private static final Map<String, String> request2Handler = new HashMap();  // 指令，spring对应bean名:方法注解名称(唯一)
    //site, userService:site

    static {
        //做指令初始化工作 eg 读取指令文本文件抑或其他途径
        request2Handler.put("site","userService:site");
    }


    /**
     * 根据requestOrder执行spring管理下的bean中有特定注解的方法
     * @param ctx
     * @param requestOrder
     * @return
     */
    public void dispatch(ChannelHandlerContext ctx, String requestOrder) {
        System.out.println("dispatch" + requestOrder);
        executorService.submit(()->{
            String response = "";
            ChannelFuture future = null;
            try {
                String beanAndOrder = request2Handler.get(requestOrder);
                if(beanAndOrder == null) {
                    future = ctx.writeAndFlush("无此指令");
                    return;
                }
                String beanName = beanAndOrder.split(":")[0];
                String orderName =  beanAndOrder.split(":")[1];
                Object bean = SpringUtil.getApplicationContext().getBean(beanName);
                if(beanName == null || beanName.isEmpty() || bean == null) {
                    future = ctx.writeAndFlush(NullWritable.nullWritable());
                } else {
                    Method[] methods = bean.getClass().getDeclaredMethods();
                    for (Method method : methods) {
                        GameOrderMapping order = method.getAnnotation(GameOrderMapping.class);
                        if(order != null && order.value().equals(orderName)) {
                            response = (String) method.invoke(bean);
                            break;
                        }
                    }
                }
                if (response == null || "".equals(response)) {
                    future = ctx.writeAndFlush(NullWritable.nullWritable());
                } else {
                    future = ctx.writeAndFlush(response);
                }
            } catch (Exception e) {
                future = ctx.writeAndFlush("svr err occur, please reconnect");
                e.printStackTrace();
                future.addListener(ChannelFutureListener.CLOSE);
            }
        });
    }
}

