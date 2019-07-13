package org.sq.gameDemo.svr.common.protoUtil;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface ProtoObject {
    boolean Ignore() default false;
    Class TargetClass() default Void.class;
    String TargetRepeatedName() default "";
}