package com.storyteller_f.sql_query.query.expression;

/**
 * 这个类不用实现什么方法，全部有ValueQuery 类完成。
 * @param <T>
 */
public class ValueExpression<T> extends TwoExpression<T>{

    public ValueExpression(Class<?> tableClass,String fieldName, T value) {
        super(tableClass,fieldName, value);
    }
    
    public ValueExpression<T> next(ValueExpression<T> valueExpression) {
    	next=valueExpression;
    	return this;
    }
    @Override
    public String parse(boolean safe) throws Exception {
        return null;
    }
}
