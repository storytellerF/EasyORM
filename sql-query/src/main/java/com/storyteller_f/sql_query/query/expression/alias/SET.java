package com.storyteller_f.sql_query.query.expression.alias;

import com.storyteller_f.sql_query.query.GetName;
import com.storyteller_f.sql_query.query.expression.SmallThanAndEqualExpression;

public class SET<T> extends SmallThanAndEqualExpression<T> {
    public SET(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public SET(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public SET(String fieldName, T value) {
        super(fieldName, value);
    }

    public SET(GetName<String> fieldName, T value) {
        super(fieldName, value);
    }
}
