package com.storyteller_f.sql_query.query.expression.alias;

import com.storyteller_f.sql_query.query.GetName;
import com.storyteller_f.sql_query.query.expression.NotEqualExpression;

public class NEE<T> extends NotEqualExpression<T> {
    public NEE(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public NEE(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public NEE(String fieldName, T value) {
        super(fieldName, value);
    }

    public NEE(GetName<String> fieldName, T value) {
        super(fieldName, value);
    }
}
