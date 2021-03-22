package com.storyteller_f.sql_query.query.expression;

public class GreaterThanExpression<T> extends ArithMeticalExpression<T>{

    public GreaterThanExpression( Class<?> tableClass,String fieldName, T value) {
        super(tableClass,fieldName, value);
    }

    public GreaterThanExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    protected String parse(String name, String right,boolean safe) {
        if (!safe) {
            return name+" > "+right;
        }
        return name+" > ?";
    }

}
