package com.storyteller_f.sql_query.query;

import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.expression.ValueExpression;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;
import com.storyteller_f.sql_query.query.query.ValueQuery;
import com.storyteller_f.sql_query.query.type.Changer;

public class Insert<T> extends Changer<T> {
    private final ValueQuery valueQuery;
    public Insert(Obtain obtain) {
        super(obtain);
        valueQuery=new ValueQuery();
        setReturnType(Integer.class);
    }
    @Override
	public void add(Class<?> claxx, String fieldName, Object value) {
		value(new ValueExpression<>(claxx, fieldName, value));
	}
    public Insert<T> value(ValueExpression<?> valueExpression) {
        valueQuery.value(valueExpression);
        return this;
    }
    public Insert<T> insert(T t) throws Exception {
        setObject(t);
        return this;
    }
    @Override
    public String parse(boolean safe) throws Exception {
    	super.parse(safe);
        return String.format("insert into %s %s;", tableQuery.parse(safe), valueQuery.parse(safe));
    }

    @Override
    public ExpressionQuery getExpressionQuery() {
        return valueQuery.getValueExpression();
    }
}
