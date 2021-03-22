package com.storyteller_f.sql_query.query.expression.alias;

import com.storyteller_f.sql_query.query.expression.EqualExpression;

public class EE<T> extends EqualExpression<T> {
    public EE(String fieldName, T value) {
        super(fieldName, value);
    }

    public EE(Class<?> tableClass, String column, T value) {
        super(tableClass, column, value);
    }
}
