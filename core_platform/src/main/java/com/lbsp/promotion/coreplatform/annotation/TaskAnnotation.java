package com.lbsp.promotion.coreplatform.annotation;

import java.lang.annotation.*;

/**
 * Created by hp on 2014/10/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TaskAnnotation {
    public abstract  String value() default "";   //名称
}
