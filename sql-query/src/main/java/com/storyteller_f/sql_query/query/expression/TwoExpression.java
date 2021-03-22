package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.query.query.ExpressionQuery;

public class TwoExpression<T> extends ExpressionQuery {

    protected String fieldName;
    protected T value;
    protected Class<?> tableClass;
    
    public TwoExpression(Class<?> tableClass, String fieldName, T value) {
        super();
        this.fieldName = fieldName;
        this.value = value;
        this.tableClass = tableClass;
    }

    public TwoExpression(String fieldName, T value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
	public Object clone() throws CloneNotSupportedException {
    	return new TwoExpression<T>(tableClass, fieldName, value);
    }
    @Override
    public String parse(boolean safe) throws Exception {
        return null;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public void setTableClass(Class<?> tableClass) {
        this.tableClass = tableClass;
    }
    interface GetColumn<T>{
        String g(T t);
    }

}
