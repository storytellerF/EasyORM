package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.query.GetName;

public class SmallThanAndEqualExpression<T> extends ThreeExpression<T>{
    public SmallThanAndEqualExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public SmallThanAndEqualExpression(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public SmallThanAndEqualExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    public SmallThanAndEqualExpression(GetName<String> fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String getOperator() {
        return "<=";
    }
}
