package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.query.GetName;

public abstract class ArithmeticalExpression<T> extends TwoExpression<T> {

    public ArithmeticalExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }
    public ArithmeticalExpression(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName.get(), value);
    }
    public ArithmeticalExpression(String fieldName, T value) {
        super(fieldName, value);
    }
    public ArithmeticalExpression(GetName<String> fieldName, T value) {
        super(fieldName.get(), value);
    }

    @Override
    public String parse(boolean safe) throws Exception {
        String name = getName();
        String string = parse(name, this.objectToString(value), safe);
        if (next != null) {
            string += " and " + next.parse(safe);
        }
        return string;
    }

    protected abstract String parse(String name, String right, boolean safe);
}
