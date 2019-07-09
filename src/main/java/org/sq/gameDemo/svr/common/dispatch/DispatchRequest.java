package org.sq.gameDemo.svr.common.dispatch;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.sq.gameDemo.common.OrderEnum.SvrErr;

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
                    String key = value.getOrder();
                    if(request2Handler.get(key) != null) {
                        throw new Exception("指令映射出现重复");
                    } else {
                        request2Handler.put(key, new OrderBean(beanName, method));
                    }

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
        msgEntity.setChannel(ctx.channel());
        short cmdCode = msgEntity.getCmdCode();
        System.out.println("server: dispatch->" + cmdCode + ":" + OrderEnum.getOrderByCode(cmdCode));
        executorService.submit(()->{
            Object response = null;
            ChannelFuture future = null;
            OrderBean orderBean = null;
            try {
                response = getResponse(msgEntity);
                if (response == null) {
                    future = ctx.writeAndFlush(null);
                } else {
                    future = ctx.writeAndFlush(response);
                }
            } catch (Exception e) {
                //在客户端直接加个String的handler
                System.out.println("服务端异常");
                e.printStackTrace();
                future = ctx.writeAndFlush(svrErrEntity());
                //不进行关闭
                //future.addListener(ChannelFutureListener.CLOSE);
            }
        });
    }

    private Object getResponse(MsgEntity msgEntity) throws IllegalAccessException, InvocationTargetException {

        OrderBean orderBean;
        Object response;
        short cmdCode = msgEntity.getCmdCode();
        orderBean = request2Handler.get(OrderEnum.getOrderByCode(cmdCode));
//        if(orderBean == null) {
//            orderBean = request2Handler.get(OrderEnum.ErrOrder.getOrder());
//        }

        Object bean = SpringUtil.getBean(orderBean.getBeanName());
        byte[] data = msgEntity.getData();

        Method method = orderBean.getMethod();
        if(data != null && data.length != 0) {
            response = method.invoke(bean, msgEntity);
        } else {
            response = method.invoke(bean);
        }
        return response;
    }


    private Object svrErrEntity() {
        MsgEntity errEntity = new MsgEntity();
        errEntity.setCmdCode(SvrErr.getOrderCode());
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setContent("服务端异常");
        errEntity.setData(builder.build().toByteArray());
        return errEntity;
    }
}

