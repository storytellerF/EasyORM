package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.query.GetName;

public class GreaterThanAndEqualExpression<T> extends ThreeExpression<T>{
    public GreaterThanAndEqualExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public GreaterThanAndEqualExpression(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public GreaterThanAndEqualExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    public GreaterThanAndEqualExpression(GetName<String> fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String getOperator() {
        return ">=";
    }
}
