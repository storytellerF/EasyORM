package com.storyteller_f.sql_query.query;

import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.expression.EqualExpression;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;
import com.storyteller_f.sql_query.query.query.WhereQuery;
import com.storyteller_f.sql_query.query.type.Changer;

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
        return whereQuery.getHeaderPointer();
    }

    public Delete<T> and(ExpressionQuery expressionQuery) {
        whereQuery.and(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public int execute() throws Exception {
        return obtain.getInt(this);
    }
}