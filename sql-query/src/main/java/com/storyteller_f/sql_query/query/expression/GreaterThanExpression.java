package com.storyteller_f.sql_query.query.expression;

public class GreaterThanExpression<T> extends ThreeExpression<T>{

    public GreaterThanExpression( Class<?> tableClass,String fieldName, T value) {
        super(tableClass,fieldName, value);
    }

    public GreaterThanExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String getOperator() {
        return ">";
    }
}
