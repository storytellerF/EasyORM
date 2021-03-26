package com.storyteller_f.sql_query.query.expression;

public class SmallThanExpression<T> extends ThreeExpression<T>{
    public SmallThanExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    public SmallThanExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass,fieldName, value);
    }

    @Override
    public String getOperator() {
        return "<";
    }
}
