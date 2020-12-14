package org.sq.gameDemo.aov;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerInitModule {
    
    Class<? extends IPlayerDataOperate>[] after() default {};
    
    boolean customCall() default false;
}
