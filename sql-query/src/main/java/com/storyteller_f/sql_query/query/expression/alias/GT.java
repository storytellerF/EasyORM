package com.storyteller_f.sql_query.query.expression.alias;

import com.storyteller_f.sql_query.query.expression.GreaterThanExpression;

public class GT<T> extends GreaterThanExpression<T> {
    public GT(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public GT(String fieldName, T value) {
        super(fieldName, value);
    }
}
