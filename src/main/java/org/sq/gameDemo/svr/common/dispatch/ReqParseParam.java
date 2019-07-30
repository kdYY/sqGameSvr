package org.sq.gameDemo.svr.common.dispatch;

import java.lang.annotation.*;

@ProtoParam
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqParseParam {
    String value() default "";

    String name() default "";

    boolean required() default true;
}
