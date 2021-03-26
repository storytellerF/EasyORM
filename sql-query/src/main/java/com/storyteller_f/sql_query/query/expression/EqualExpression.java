package com.storyteller_f.sql_query.query.expression;

public class EqualExpression<T> extends ThreeExpression<T> {
    public EqualExpression(Class<?> tableClass,String column, T value ) {
        super(tableClass,column, value);
    }

    public EqualExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String getOperator() {
        return "=";
    }
}
