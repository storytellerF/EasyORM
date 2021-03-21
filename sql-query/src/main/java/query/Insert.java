package query;

import obtain.Obtain;
import query.expression.ValueExpression;
import query.query.ExpressionQuery;
import query.query.ValueQuery;
import query.type.Changer;

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
        return "insert into "+tableQuery.parse(safe)+" "+valueQuery.parse(safe)+";";
    }

    @Override
    public ExpressionQuery getExpressionQuery() {
        return valueQuery.getValueExpression();
    }
}
