package org.sq.gameDemo.svr.common.dispatch;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.svr.common.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//做请求转发
@Component
public class DispatchRequest{

    private ExecutorService executorService = Executors.newFixedThreadPool(NettyConstant.getMaxThreads());


    // 指令，OrderBean
    //site, {userController,method}
    private static final Map<String, OrderBean> request2Handler = new HashMap();


    /**
     * 加载指令到内存
     * @throws Exception
     */
    //@PostConstruct
    public void init() throws Exception {
        //扫描所有包下GameOrderMapping注解的方法
        //偷懒... 获取所有bean
        String[] allDefinationBeanName = SpringUtil.getAllDefinationBeanName();
        for (String beanName : allDefinationBeanName) {
            Object bean = SpringUtil.getBean(beanName);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                OrderMapping order = method.getAnnotation(OrderMapping.class);
                if(order != null) {
                    OrderEnum value = order.value();
                    request2Handler.put(value.getOrder(), new OrderBean(beanName, method));
                }
            }
        }
        System.out.println("指令加载完毕");
    }

    public static void dispatchRequest(ChannelHandlerContext ctx, MsgEntity msgEntity) {
        SpringUtil.getBean(DispatchRequest.class).dispatch(ctx, msgEntity);
    }


    // TODO 改造成Queue进行消费处理也不错
    /**
     * 根据requestOrder，分发执行方法
     * @param ctx
     * @param msgEntity 请求实体
     */
    public void dispatch(ChannelHandlerContext ctx, MsgEntity msgEntity) {
        short cmdCode = msgEntity.getCmdCode();
        System.out.println("server: dispatch->" + cmdCode);
        executorService.submit(()->{
            Object response = null;
            ChannelFuture future = null;
            try {
                OrderBean orderBean = request2Handler.get(OrderEnum.getOrder(cmdCode));
                if(orderBean == null) {
                    orderBean = request2Handler.get(OrderEnum.ErrOrder.getOrder());
                }

                Object bean = SpringUtil.getBean(orderBean.getBeanName());
                byte[] data = msgEntity.getData();

                Method method = orderBean.getMethod();
                if(data != null && data.length != 0) {
                    //TODO 获取方法中的参数名，参数类型，将msgEntity.getData中的参数进行绑定，发现参数异常返回err
                    //register name=kevins&password=123456
                    //login name=kevins&password=123456
                    //move sence=1

                    response = method.invoke(bean, msgEntity);
                } else {
                    response = method.invoke(bean);
                }

                if (response == null) {
                    future = ctx.writeAndFlush(null);
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

