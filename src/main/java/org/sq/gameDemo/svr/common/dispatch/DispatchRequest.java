package org.sq.gameDemo.svr.common.dispatch;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.*;
import org.sq.gameDemo.svr.common.customException.CustomException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
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


    /**
     * 根据requestOrder，分发执行方法
     * @param ctx
     * @param msgEntity 请求实体
     */
    public void dispatch(ChannelHandlerContext ctx, MsgEntity msgEntity) {
        msgEntity.setChannel(ctx.channel());
        short cmdCode = msgEntity.getCmdCode();
        if(OrderEnum.PING.getOrderCode() == cmdCode) {
            return;
        }
        System.out.println("server: dispatch->" + cmdCode + ":" + OrderEnum.getOrderByCode(cmdCode));
        executorService.submit(()->{
            Object response = null;
            ChannelFuture future = null;
            OrderBean orderBean = null;
            try {
                response = getResponse(msgEntity);
                if (response != null) {
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

    /**
     * 获取方法执行后的返回值
     * @param msgEntity
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object getResponse(MsgEntity msgEntity) throws IllegalAccessException, InvocationTargetException {

        OrderBean orderBean;
        Object response = null;
        short cmdCode = msgEntity.getCmdCode();
        orderBean = request2Handler.get(OrderEnum.getOrderByCode(cmdCode));
        if(Objects.nonNull(orderBean)) {
            Object bean = SpringUtil.getBean(orderBean.getBeanName());

            Method method = orderBean.getMethod();
            //没有参数
            if(method.getParameterCount() == 0) {
                response = method.invoke(bean);
            }
            //有参数
            else {
                List<Object> requiredParamList = new ArrayList<>();

                Arrays.stream(method.getParameters()).forEach(
                    parameter -> {
                        Class<?> paramType = parameter.getType();

                        if(paramType.equals(MsgEntity.class)) {
                            requiredParamList.add(msgEntity);
                        } else {
                            /**
                             * 获取注解上有ProtoParam注解的所有注解，并添加实例化参数
                             */
                            Arrays.stream(parameter.getDeclaredAnnotations()).forEach(
                                paramAnno -> {
                                    ProtoParam type = paramAnno.annotationType().getDeclaredAnnotation(ProtoParam.class);
                                    if(type != null) {
                                        Optional.ofNullable( getParamInjectObj(paramType, paramAnno, msgEntity) )
                                                .ifPresent(obj -> requiredParamList.add(obj));
                                    }
                            });

                        }
                    }
                );
                if(requiredParamList.size() != 0) {
                    response = method.invoke(bean, requiredParamList.toArray());
                } else {
                    throw new CustomException.ParamNoMatchException("请求参数不匹配");
                }

            }
        }

        return response;
    }


    private Object  getParamInjectObj(Class paramType, Annotation paramAnno, MsgEntity msgEntity) {
        if(Objects.nonNull(paramAnno)) {
            if(paramAnno.annotationType().equals(RespBuilderParam.class)) {
                String requestProto = paramType.getTypeName();
                String build = requestProto.substring(0, requestProto.lastIndexOf("$"));
                try {
                    return invokeStaticMethod(Class.forName(build), "newBuilder");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(paramAnno.annotationType().equals(ReqParseParam.class)) {
                    return invokeStaticMethod(paramType, "parseFrom", msgEntity);

            }
        }
        return null;

    }

    private Object invokeStaticMethod(Class<?> clazz, String methodName, Object... msgEntity) {
        try {
            Method method = null;
            if(msgEntity != null &&msgEntity.length != 0) {
                method = clazz.getDeclaredMethod(methodName, ((MsgEntity)msgEntity[0]).getData().getClass());
                return method.invoke(null, ((MsgEntity)msgEntity[0]).getData());
            } else {

                method = clazz.getDeclaredMethod(methodName);
                return method.invoke(null);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Object svrErrEntity() {
        MsgEntity errEntity = new MsgEntity();
        errEntity.setCmdCode(SvrErr.getOrderCode());
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setContent("服务端异常");
        errEntity.setData(builder.build().toByteArray());
        return errEntity;
    }

    public static void main(String[] args) {
        AnnotatedType[] annotatedInterfaces = ReqParseParam.class.getAnnotatedInterfaces();
        Annotation[] annotations = ReqParseParam.class.getAnnotations();
        Annotation[] declaredAnnotations = ReqParseParam.class.getDeclaredAnnotations();
        ProtoParam declaredAnnotation = ReqParseParam.class.getDeclaredAnnotation(ProtoParam.class);
        Class<? extends Annotation> aClass = ReqParseParam.class;
        ProtoParam declaredAnnotation1 = aClass.getDeclaredAnnotation(ProtoParam.class);
        Field[] declaredFields = ReqParseParam.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            declaredField.getAnnotations();
        }
    }
}

