package org.sq.gameDemo.svr.common.dispatch;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqParseProto {
    String value() default "";

    String name() default "";

    boolean required() default true;
}
