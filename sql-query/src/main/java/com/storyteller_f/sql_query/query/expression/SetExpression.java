package com.storyteller_f.sql_query.query.expression;

public class SetExpression<T> extends TwoExpression<T>{
    public SetExpression(Class<?> tableClass,String fieldName, T value) {
        super(tableClass,fieldName, value);
    }
    @Override
    public String parse(boolean safe) throws Exception {
        String name = getName();
        String string = parse(name, objectToString(value),safe);
        if (next != null) {
            string += String.format(" , %s", next.parse(safe));
        }
        return string;
    }

    @Override
	public Object clone() {
        return new SetExpression<T>(tableClass, fieldName, value);
    }

    protected String parse(String name, String right,boolean safe) {
        if (safe) {
            return "" + name + " = ?";
        } else {
            return "" + name + " = "+(right==null?"null":right);

        }
    }
    public SetExpression<T> comma(SetExpression<?> setQuery) {
        next=setQuery;
        return this;
    }
}
