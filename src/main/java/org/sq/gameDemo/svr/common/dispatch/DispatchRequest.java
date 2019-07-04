package org.sq.gameDemo.svr.common.dispatch;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.*;

import java.lang.reflect.Method;
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
                    Order value = order.value();
                    request2Handler.put(value.getOrder(), new OrderBean(beanName, method));
                }
            }
        }
        System.out.println("指令加载完毕");
//        List<OrderRule> orderRuleList = orderRuleService.getOrderRuleList();
//        for (OrderRule orderRule : orderRuleList) {
//            if(!orderRule.getRule().contains(":")) {
//                throw new Exception("指令初始化失败，错误指令:" + orderRule.toString());
//            }
//            request2Handler.put(orderRule.getOrderName(), orderRule.getRule());
//        }

    }

    /**
     * 根据requestOrder，分发执行方法
     * @param ctx
     * @param requestOrder 指令内容
     * @param objs  参数列
     */
    public void dispatch(ChannelHandlerContext ctx, String requestOrder, Object... objs) {
        System.out.println("server: dispatch->" + requestOrder);
        executorService.submit(()->{
            Object response = null;
            ChannelFuture future = null;
            try {
                OrderBean orderBean = request2Handler.get(requestOrder);
                // TODO
                if(orderBean == null) {
                    orderBean = request2Handler.get(Order.ErrOrder.getOrder());
                }
                Object bean = SpringUtil.getBean(orderBean.getBeanName());
                response = orderBean.getMethod().invoke(bean, objs);

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

    public static void dispatchRequest(ChannelHandlerContext ctx, String order, Object... objs) {
        SpringUtil.getBean(DispatchRequest.class).dispatch(ctx, order, objs);
    }

}

