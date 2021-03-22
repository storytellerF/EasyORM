package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.util.ORMUtil;

public class ValueExpression<T> extends TwoExpression<T>{

    public ValueExpression( Class<?> tableClass,String fieldName, T value) {
        super(tableClass,fieldName, value);
    }
    
    public ValueExpression<T> next(ValueExpression<T> valueExpression) {
    	next=valueExpression;
    	return this;
    }
    @Override
    public String parse(boolean safe) throws Exception {
        String name;
        try {
            name = ORMUtil.getColumn(tableClass.getDeclaredField(fieldName));
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            name=this.fieldName;
        }
        String string = parse(name, objectToString(value));
        if (next != null) {
            string += " and " + next.parse(safe);
        }
        return string;
    }
    private String parse(String name, String objectToString) {
        return null;
    }
}
