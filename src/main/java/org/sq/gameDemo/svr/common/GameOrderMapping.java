package org.sq.gameDemo.svr.common;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GameOrderMapping {
    Order value();
}
