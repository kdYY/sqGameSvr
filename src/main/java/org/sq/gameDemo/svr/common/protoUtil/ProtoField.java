package org.sq.gameDemo.svr.common.protoUtil;


import java.lang.annotation.*;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface ProtoField {
    boolean Ignore() default false;
    Class TargetClass() default Void.class;
    String TargetName() default "";

    String Function() default "";
}