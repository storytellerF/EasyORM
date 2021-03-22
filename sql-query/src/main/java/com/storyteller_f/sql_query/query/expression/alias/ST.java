package com.storyteller_f.sql_query.query.expression.alias;

import com.storyteller_f.sql_query.query.expression.SmallThanExpression;

public class ST<T> extends SmallThanExpression<T> {
    public ST(String fieldName, T value) {
        super(fieldName, value);
    }

    public ST(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }
}
