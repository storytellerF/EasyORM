package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.util.ORMUtil;

public class ArithMeticalExpression<T> extends TwoExpression<T> {

    public ArithMeticalExpression(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public ArithMeticalExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    public String parse(boolean safe) throws Exception {
        String string;
        if (tableClass != null) {
            String name = ORMUtil.getTrueSqlColumn(tableClass, fieldName, tableMap);
            string = parse(name, this.objectToString(value), safe);
        } else {
            string = parse(fieldName, this.objectToString(value), safe);
        }

        if (next != null) {
            string += " and " + next.parse(safe);
        }
        return string;
    }

    protected String parse(String name, String right, boolean safe) {
        return "";
    }

}
