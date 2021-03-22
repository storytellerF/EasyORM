package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.util.ORMUtil;

public class SetExpression<T> extends TwoExpression<T>{
    public SetExpression(Class<?> tableClass,String fieldName, T value) {
        super(tableClass,fieldName, value);
    }
    @Override
    public String parse(boolean safe) throws Exception {
        String name;
        try {
            name ="`"+ ORMUtil.getColumn(tableClass.getDeclaredField(fieldName))+"`";
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            name="`"+this.fieldName +"`";
        }
        String string = parse(name, objectToString(value));
        if (next != null) {
            string += " , " + next.parse(safe);
        }
        return string;
    }
    @Override
	public Object clone() {
        return new SetExpression<T>(tableClass, fieldName, value);
    }
    protected String parse(String name, String right) {
        return ""+name+" = ?";
    }
    public SetExpression<T> comma(SetExpression<?> setQuery) {
        next=setQuery;
        return this;
    }
}
