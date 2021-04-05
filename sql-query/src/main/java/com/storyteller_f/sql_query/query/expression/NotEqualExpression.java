package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.query.GetName;
import com.storyteller_f.sql_query.query.expression.ThreeExpression;

public class NotEqualExpression<T> extends ThreeExpression<T> {
    public NotEqualExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public NotEqualExpression(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public NotEqualExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    public NotEqualExpression(GetName<String> fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String getOperator() {
        return "!=";
    }
}
