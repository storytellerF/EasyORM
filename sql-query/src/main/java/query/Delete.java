package query;

import obtain.Obtain;
import query.expression.EqualExpression;
import query.query.ExpressionQuery;
import query.query.WhereQuery;
import query.type.Changer;

public class Delete<T> extends Changer<T> {
    private final WhereQuery whereQuery;

    public Delete(Obtain ob) {
        super(ob);
        whereQuery = new WhereQuery();
        setReturnType(Integer.class);
    }

    @Override
    public void add(Class<?> claxx, String fieldName, Object value) {
        and(new EqualExpression<>(claxx, fieldName, value));
    }

    public Delete<T> delete(T t) throws Exception {
        setObject(t);
        return this;
    }

    @Override
    public String parse(boolean safe) throws Exception {
        super.parse(safe);
        return "delete from " + tableQuery.parse(safe) + " " + whereQuery.parse(safe) + ";";
    }

    @Override
    public ExpressionQuery getExpressionQuery() {
        return whereQuery.getExpression();
    }

    public Delete<T> and(ExpressionQuery expressionQuery) {
        whereQuery.and(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public int execute() throws Exception {
        return obtain.getInt(this);
    }
}
