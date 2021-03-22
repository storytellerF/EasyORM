package com.storyteller_f.sql_query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Convert {
	String name();//需要传递一个函数名
	String reverse() default "";//拥有默认值，当转换前的类型和转换后的类型不同时，默认和name相同，如果类型不同时，默认未name颠倒顺序之后的值
}
