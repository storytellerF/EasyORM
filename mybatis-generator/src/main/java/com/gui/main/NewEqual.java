package com.gui.main;

import com.storyteller_f.sql_query.query.expression.EqualExpression;

public class NewEqual<T> extends EqualExpression<T> {
    public NewEqual(Class<?> tableClass, String column, T value) {
        super(tableClass, column, value);
    }

    public NewEqual(String fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String objectToString(Object value) {
        System.out.println(value);
        return (String) value;
    }
}
