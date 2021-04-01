package com.storyteller_f.sql_query.query;

import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.expression.ValueExpression;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;
import com.storyteller_f.sql_query.query.query.ValueQuery;
import com.storyteller_f.sql_query.query.type.Changer;

public class Insert<OBJECT_TYPE> extends Changer<OBJECT_TYPE> {
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
    public Insert<OBJECT_TYPE> value(ValueExpression<?> valueExpression) {
        valueQuery.value(valueExpression);
        return this;
    }
    public Insert<OBJECT_TYPE> insert(OBJECT_TYPE t) throws Exception {
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
