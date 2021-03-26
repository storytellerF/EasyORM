package com.storyteller_f.sql_query.query.expression;

public abstract class ThreeExpression <T> extends ArithMeticalExpression<T>{
    public ThreeExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public ThreeExpression(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public ThreeExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    public ThreeExpression(GetName<String> fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    protected String parse(String column, String right,boolean safe) {
        if (!safe) {
            return column+" "+ getOperator()+" " +right;
        }
        return column+" = ?";
    }

    public abstract String getOperator();
}
