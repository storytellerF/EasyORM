package com.storyteller_f.sql_query.annotation.type.verify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * *仅用于生成html表单
 * @author lava
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Equal {
	String id();//控件的id，基本上就是列的名字
}
