package com.storyteller_f.sql_query.annotation;

import java.lang.annotation.*;

/**
 *当前字段在数据库中并不存在
 * @author lava
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoQuery {

}
