package com.lbsp.promotion.entity.table.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTable {
	public abstract String value();

	public abstract String primaryKey() default "id";

    public abstract String notUpdateField() default "create_date,create_user";

    public abstract String filterKey() default "mysql";

}
