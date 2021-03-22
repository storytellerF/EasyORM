package com.storyteller_f.sql_query.query.expression;

public class SmallThanExpression<T> extends ArithMeticalExpression<T>{
    public SmallThanExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    public SmallThanExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass,fieldName, value);
    }
    @Override
    protected String parse(String name, String right,boolean safe) {
        if (!safe) {
            return name+" < "+right;
        }
        return name+" < ?";
    }

}
