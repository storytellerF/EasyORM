package com.storyteller_f.sql_query.query.expression;

import java.util.Date;

public class LikeExpression<T> extends ThreeExpression<T>{
    public LikeExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public LikeExpression(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public LikeExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    public LikeExpression(GetName<String> fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String getOperator() {
        return "like";
    }

    @Override
    public String objectToString(Object value) {
        if (value==null) return null;
        if (value instanceof String) {
            return "'%" + value + "%'";
        } else if (value instanceof Integer) {
            return "'%" + value + "%'";
        }else if (value instanceof Date){
            return simpleDateFormat.format(value);
        }
        throw new IllegalArgumentException("不支持的类型:" + value.getClass());
    }
}
