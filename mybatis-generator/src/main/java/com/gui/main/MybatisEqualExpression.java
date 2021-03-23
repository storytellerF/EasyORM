package com.gui.main;

import com.storyteller_f.sql_query.query.expression.EqualExpression;

public class MybatisEqualExpression<T> extends EqualExpression<T> {
    public MybatisEqualExpression(Class<?> tableClass, String column, T value) {
        super(tableClass, column, value);
    }

    public MybatisEqualExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String objectToString(Object value) {
        return (String) value;
    }
}
