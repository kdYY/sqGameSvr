package org.sq.gameDemo.svr.common.protoUtil;

import lombok.extern.slf4j.Slf4j;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ProtoBufUtil {
    public static List<String> baseTypeList =
            Arrays.asList("int", "Integer", "float", "Float", "long", "Long", "double", "Double", "String", "Boolean", "boolean");

    public static  <T,K> Object transformProtoReturnBean(T goalBuilder, K sourceBean) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        try{
            transformProtoReturnBuilder(goalBuilder, sourceBean);
            Method build = goalBuilder.getClass().getDeclaredMethod("build");
            return build.invoke(goalBuilder);
        }catch (Exception e) {
            return null;
        }

    }
    //
    public static <T,K> T transformProtoReturnBuilder(T goalBuilder, K sourceBean) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Method[] goalBuilderMethod = goalBuilder.getClass().getDeclaredMethods();
        Map<String, ProtoField> feildNameIgnoreMap = new HashMap<>();
        Map<Field, Class> listClassMap = new HashMap<>();

        Map<Field, Method> listGetMethodMap = new HashMap<>();
        Map<Field, Method> goalBuilderAddMethodMap = new HashMap<>();
        Map<Field, Method> goalBuildBaseTypeAddMethodMap = new HashMap<>();

        Map<Field, Method> sourceBeanGetMethodMap = new HashMap<>();
        Map<Field, Method> goalBuilderSetMethodMap = new HashMap<>();


        Map<Field, Method> sourceBeanFunctionMap = new HashMap<>();

        //获取K中的所有属性名称，排除@TransferProto(ignore=false)的属性, 获取需要注入List的属性
        List<Field> declaredFields = getClassField(sourceBean.getClass());//.getFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);

            boolean inject = true;
            Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
            String fieldName = declaredField.getName();
            String getMethodName = "get" + upperCaseFirstLetter(fieldName);
            String setMethodName = "set" + upperCaseFirstLetter(fieldName.replaceAll("_", ""));


            for (Annotation declaredAnnotation : declaredAnnotations) {
                if(declaredAnnotation instanceof ProtoField) {
                    ProtoField annotation = (ProtoField) declaredAnnotation;
                    //查看ignore屬性
                    if(annotation.Ignore()) {
                        feildNameIgnoreMap.put(fieldName, annotation);
                        inject = false;
                        break;
                    }
                    Class targetClass = annotation.TargetClass();
                    //如果K中有Function需要执行的，加入执行
                    if(!annotation.Function().isEmpty()) {
                        Method method;
                        if(!targetClass.equals(Void.class)
                                && !annotation.TargetName().isEmpty()) {
                            method = sourceBean.getClass().getMethod(annotation.Function(), targetClass);
                        } else {
                            method = sourceBean.getClass().getDeclaredMethod(annotation.Function());
                        }
                        sourceBeanFunctionMap.put(declaredField, method);
                        inject = false;
                        break;
                    }

                    //如果K中有List,检查declaredMethods是否有add的方法，没有则跳过，有则加入执行
                    Method addMethod = null;

                    String simpleName = targetClass.getSimpleName();
                    if(baseTypeList.contains(simpleName)) {
                        //基础类型的class
                        String targetName = "add" + upperCaseFirstLetter(annotation.TargetName());
                        if((addMethod = hasListAddMethond(goalBuilderMethod, targetName, targetClass)) != null) {
                            listClassMap.put(declaredField, targetClass);
                            //list的get方法
                            listGetMethodMap.put(declaredField, getMethod(sourceBean, getMethodName));
                            //add方法
                            goalBuildBaseTypeAddMethodMap.put(declaredField, addMethod);
                        }
                    } else {
                        String addMethodName = "add" + upperCaseFirstLetter(simpleName);
                        if(!targetClass.equals(Void.class)
                                && (addMethod = hasListAddMethond(goalBuilderMethod, addMethodName, targetClass)) != null) {
                            listClassMap.put(declaredField, targetClass);
                            //list的get方法
                            listGetMethodMap.put(declaredField, getMethod(sourceBean, getMethodName));
                            //add方法
                            goalBuilderAddMethodMap.put(declaredField, addMethod);
                        }
                    }
                    inject = false;
                    break;
                }
            }
            //正常需要注入的屬性
            if(inject) {
                //进行K的get方法的获取，Map记录<属性名，get属性()>
                //T的set方法获取，Map记录<K有的属性名， set属性()>
                try {
                    Method getMethod = getMethod(sourceBean, getMethodName);
                    if(getMethod.getReturnType().equals(declaredField.getType())) {
                        Method setMethod = getMethod(goalBuilder,  setMethodName, declaredField.getType());
                        if(getMethod != null && setMethod != null) { //set方法中setXXX(int)和setXXX(Integer)不一样，需要对基本类型做一个兼容
                            sourceBeanGetMethodMap.put(declaredField, getMethod);
                            goalBuilderSetMethodMap.put(declaredField, setMethod);
                        }
                    }

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
        }
        //进行方法执行
        for (Map.Entry<Field, Method>  getMethodEntry: sourceBeanGetMethodMap.entrySet()) {
            Method getMethod = getMethodEntry.getValue();
            Field field = getMethodEntry.getKey();
            field.setAccessible(true);
            Method setMethod = goalBuilderSetMethodMap.get(field);
            Object getValue = getMethod.invoke(sourceBean);
            if(getValue != null) {
                try {
                    setMethod.invoke(goalBuilder, getValue);
                } catch (java.lang.IllegalArgumentException e) {
                    e.printStackTrace();
                    log.info(getMethod.getName() + "中类型为" + getMethod.getReturnType().getName());
                    log.info(setMethod.getName() + "中类型为" + setMethod.getParameterTypes()[0].getName());
                    log.info("类型不一致");
                }
            } else {
//                log.info(setMethod.getName() + "的参数来自" + field.getName() + ", 参数内容为空, get的域为" + field.getName() + ";set的域来自" + goalBuilder.getClass().getName() );
            }

        }

        for (Map.Entry<Field, Method> fieldMethodEntry : sourceBeanFunctionMap.entrySet()) {
            Method method = fieldMethodEntry.getValue();
            method.invoke(sourceBean, goalBuilder);
        }
        //return goalBuilder.setId(sence.getId()).setName(sence.getName()).build();

        for (Map.Entry<Field, Class> entry : listClassMap.entrySet()) {
            Field field = entry.getKey();
            Class listClass = entry.getValue();
            //get方法
            Method getListMethod = listGetMethodMap.get(field);
            List invoke = (List) getListMethod.invoke(sourceBean);
            if(invoke != null) {
                if(baseTypeList.contains(listClass.getSimpleName())) {
                    for (Object o : invoke) {
                        goalBuildBaseTypeAddMethodMap.get(field).invoke(goalBuilder, o);
                    }
                    continue;
                }
                for (int i = 0; i < invoke.size(); i++) {
                    try {
                        Constructor cellConstruct = listClass.getDeclaredConstructor();
                        cellConstruct.setAccessible(true);
                        Object o = cellConstruct.newInstance();
                        Object newBuilder = getMethod(o, "newBuilder").invoke(null);
                        goalBuilderAddMethodMap.get(field).invoke(goalBuilder, transformProtoReturnBean(newBuilder, invoke.get(i)));
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                        throw e;
                    }

                }
            }

        }
        return goalBuilder;
    }

    //对java API中的获取方法做一个基本类型的兼容
    private static <T> Method getMethod(T goalBuilder, String methodName, Class<?>... type) throws NoSuchMethodException {
        if(type == null || type.length == 0) {
            return getDeclaredMethod(goalBuilder.getClass(), methodName);
        } else if(type.length == 1) {
            String typeName = type[0].getName();
            try {
                Method declaredMethod = getDeclaredMethod(goalBuilder.getClass(), methodName, type[0]);
                return declaredMethod;
            } catch (NoSuchMethodException e) {
                List<Method> declaredMethods = getClassMethod(goalBuilder.getClass(), methodName);
                for (Method declaredMethod : declaredMethods) {
                    Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                    if(parameterTypes.length == 1 && baseTypeList.contains(parameterTypes[0].getName())) {
                        return declaredMethod;
                    }
                }
                e.printStackTrace();
                throw  new NoSuchMethodException("在"+ goalBuilder.getClass().getName() +"中没有"+ methodName +"方法，请检查proto文件是否跟bean定义的字段一致");
            }
        } else {
            return  getDeclaredMethod(goalBuilder.getClass(),methodName, type);
        }

    }

    private static Method hasListAddMethond(Method[] declaredMethods, String targetAddMethodName, Class targetClass) {
        for (Method method : declaredMethods) {
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            if(method.getName().equals(targetAddMethodName)
                    && method.getParameterTypes().length == 1
                    && genericParameterTypes[0].getTypeName().equals(targetClass.getName())) {
                return method;
            }
        }
        return null;
    }

    private static String upperCaseFirstLetter(String word) {
        try {
            return String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(word);
            throw e;
        }

    }

    static List<Field> getClassField(Class cur_class) {
        String class_name = cur_class.getName();
        Field[] obj_fields = cur_class.getDeclaredFields();
        List<Field> collect = Arrays.stream(obj_fields).collect(Collectors.toList());
        //Method[] methods = cur_class.getDeclaredMethods();

        if (cur_class.getSuperclass() != null && cur_class.getSuperclass() != Object.class) {
            collect.addAll(getClassField(cur_class.getSuperclass()));
        }
        return collect;
    }

    static List<Method> getClassMethod(Class cur_class, String methodName) {
        Method[] methods = cur_class.getDeclaredMethods();
        List<Method> collect = Arrays.stream(methods)
                .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

        if (cur_class.getSuperclass() != null && cur_class.getSuperclass() != Object.class) {
            collect.addAll(getClassMethod(cur_class.getSuperclass(), methodName));
        }
        return collect;
    }


    static Method getDeclaredMethod(Class cur_class, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException{
        Method result = null;
        try {
            result = cur_class.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
           // e.printStackTrace();
        }
        if (result == null && (cur_class.getSuperclass() != null && cur_class.getSuperclass() != Object.class)) {
            result = getDeclaredMethod(cur_class.getSuperclass(), methodName);
            if(result == null) {
                throw new NoSuchMethodException("在" + cur_class.getName() + "中递归找不到该方法" + methodName);
            }
        }
        return result;
    }

    static List<Annotation> getClassAnnotation(Class cur_class) {
        String class_name = cur_class.getName();
        Annotation[] annotations = cur_class.getDeclaredAnnotations();
        List<Annotation> collect = Arrays.stream(annotations).collect(Collectors.toList());

        if (cur_class.getSuperclass() != null && cur_class.getSuperclass() != Object.class ) {
            collect.addAll(getClassAnnotation(cur_class.getSuperclass()));
        }
        return collect;
    }

    //返回广播信息体
    public static MsgEntity getBroadCastEntity(byte[] protoByte) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setData(protoByte);
        msgEntity.setCmdCode(OrderEnum.BroadCast.getOrderCode());
        return msgEntity;
    }

    //返回广播信息体
    public static MsgEntity getBroadCastDefaultEntity(String broadCastContend) {
        byte[] protoByte = MessageProto.Msg.newBuilder().setContent(broadCastContend).build().toByteArray();
        return getBroadCastEntity(protoByte);
    }

    public static MsgEntity getDefaultEntity(String content) {
        return null;
    }
}
