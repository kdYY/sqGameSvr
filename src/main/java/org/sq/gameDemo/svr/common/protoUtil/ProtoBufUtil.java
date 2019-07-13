package org.sq.gameDemo.svr.common.protoUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class ProtoBufUtil {
    public static List<String> baseTypeList = Arrays.asList("int", "Integer", "float", "Float", "double", "Double", "byte", "Byte");


    public static  <T,K> Object transformProtoReturnBean(T goalBuilder, K sourceBean) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        transformProtoReturnBuilder(goalBuilder, sourceBean);
        Method build = goalBuilder.getClass().getDeclaredMethod("build");
        return build.invoke(goalBuilder);
    }
    //
    public static <T,K> T transformProtoReturnBuilder(T goalBuilder, K sourceBean) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method[] goalBuilderMethod = goalBuilder.getClass().getDeclaredMethods();
        Method[] sourceBeanMethods = sourceBean.getClass().getDeclaredMethods();
        Map<String, ProtoObject> feildNameIgnoreMap = new HashMap<>();
        Map<Field, Class> listClassMap = new HashMap<>();
        Map<Field, Method> listGetMethodMap = new HashMap<>();
        Map<Field, Method> goalBuilderAddMethodMap = new HashMap<>();
        Map<Field, Method> sourceBeanGetMethodMap = new HashMap<>();
        Map<Field, Method> goalBuilderSetMethodMap = new HashMap<>();
        //获取K中的所有属性名称，排除@TransferProto(ignore=false)的属性, 获取需要注入List的属性
        Field[] declaredFields = sourceBean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            boolean inject = true;
            Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
            String fieldName = declaredField.getName();
            String getMethodName = "get" + upperCaseFirstLetter(fieldName);
            String setMethodName = "set" + upperCaseFirstLetter(fieldName);


            for (Annotation declaredAnnotation : declaredAnnotations) {
                if(declaredAnnotation instanceof ProtoObject) {
                    ProtoObject annotation = (ProtoObject) declaredAnnotation;
                    //查看ignore屬性
                    if(annotation.Ignore()) {
                        feildNameIgnoreMap.put(fieldName, annotation);
                        inject = false;
                        break;
                    }
                    //如果K中有List,检查declaredMethods是否有add的方法，没有则跳过，有则加入执行
                    String targetRepeatedName = annotation.TargetRepeatedName();
                    Class targetClass = annotation.TargetClass();
                    String addMethodName = "add" + upperCaseFirstLetter(targetRepeatedName);
                    if(!targetClass.equals(Void.class)
                            && targetRepeatedName != null
                            && hasListAddMethond(goalBuilderMethod, targetRepeatedName, targetClass)) {
                        listClassMap.put(declaredField, targetClass);
                        //list的get方法
                        listGetMethodMap.put(declaredField, getMethod(sourceBean, getMethodName));
                        //add方法
                        goalBuilderAddMethodMap.put(declaredField, getMethod(goalBuilder, addMethodName, targetClass));
                        inject = false;
                        break;
                    }
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
        Set<Map.Entry<Field, Method>> getMethodSet = sourceBeanGetMethodMap.entrySet();
        for (Map.Entry<Field, Method>  getMethodEntry: getMethodSet) {
            Method getMethod = getMethodEntry.getValue();
            Method setMethod = goalBuilderSetMethodMap.get(getMethodEntry.getKey());
            Object invoke = getMethod.invoke(sourceBean);
            setMethod.invoke(goalBuilder, invoke);
        }
        //return goalBuilder.setId(sence.getId()).setName(sence.getName()).build();

        for (Map.Entry<Field, Class> entry : listClassMap.entrySet()) {
            Field field = entry.getKey();
            Class listClass = entry.getValue();
            Method method = listGetMethodMap.get(field);
            List<Object> invoke = (List<Object>) method.invoke(sourceBean);
            for (int i = 0; i < invoke.size(); i++) {
                try {
                    Constructor cellConstruct = listClass.getDeclaredConstructor();
                    cellConstruct.setAccessible(true);
                    Object o = cellConstruct.newInstance();
                    Object newBuilder = getMethod(listClass, "toBuilder").invoke(o);
                    transformProtoReturnBuilder(newBuilder, invoke.get(i));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }
        return goalBuilder;
    }

    //对java API中的获取方法做一个基本类型的兼容
    private static <T> Method getMethod(T goalBuilder, String methodName, Class<?>... type) throws NoSuchMethodException {
        if(type == null || type.length == 0) {
            return goalBuilder.getClass().getDeclaredMethod(methodName);
        } else if(type.length == 1) {
            String typeName = type[0].getName();
            try {
                Method declaredMethod = goalBuilder.getClass().getDeclaredMethod(methodName, type[0]);
                if(declaredMethod != null) {
                    return declaredMethod;
                }
            } catch (NoSuchMethodException e) {
                Method[] declaredMethods = goalBuilder.getClass().getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                    if(declaredMethod.getName().equals(methodName) && parameterTypes.length == 1 && baseTypeList.contains(parameterTypes[0])) {
                        return declaredMethod;
                    }
                }
            }
            throw  new NoSuchMethodException("没有此方法");
        } else {
            return  goalBuilder.getClass().getDeclaredMethod(methodName, type);
        }

    }

    private static boolean hasListAddMethond(Method[] declaredMethods, String targetRepeatedName, Class targetClass) {
        String targetAddMethodName = "add" + upperCaseFirstLetter(targetRepeatedName);
        for (Method method : declaredMethods) {
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            if(method.getName().equals(targetAddMethodName)
                    && method.getParameterTypes().length == 1
                    && genericParameterTypes[0].getTypeName().equals(targetClass.getName())) {
                return true;
            }
        }
        return false;
    }

    private static String upperCaseFirstLetter(String word) {
        return String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1);
    }
    
}
