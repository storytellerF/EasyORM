package com.storyteller_f.sql_query.annotation.type.fixed_point;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DecimalColumn {
    int precision() default 10;
    int scale() default 0;
}
