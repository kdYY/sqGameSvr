package org.sq.gameDemo.svr.common;

import org.sq.gameDemo.common.OrderEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderMapping {
    OrderEnum value();
}
