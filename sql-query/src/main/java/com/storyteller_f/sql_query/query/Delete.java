package com.storyteller_f.sql_query.query;

import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.expression.EqualExpression;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;
import com.storyteller_f.sql_query.query.query.WhereQuery;
import com.storyteller_f.sql_query.query.type.Changer;

import java.lang.reflect.Field;

/**
 * 除非使用delete 方法，必须指定表名
 * @param <OBJECT_TYPE>
 */
public class Delete<OBJECT_TYPE> extends Changer<OBJECT_TYPE> {
    private final WhereQuery whereQuery;

    public Delete(Obtain ob) {
        super(ob);
        whereQuery = new WhereQuery();
    }

    @Override
    public void add(Class<?> claxx, String fieldName, Object value) {
        //and(new EqualExpression<>(claxx, fieldName, value));
    }

    public Delete<OBJECT_TYPE> delete(OBJECT_TYPE t) throws Exception {
        setObject(t);
        return this;
    }

    @Override
    public boolean pass(Field field, String columnName, Object value) {
        if (field.isAnnotationPresent(PrimaryKey.class)) {
            and(new EqualExpression<>(field.getDeclaringClass(), columnName, value));
            return true;
        }
        return false;
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

    /**
     * 给where 设定条件
     * @param expressionQuery
     * @return
     */
    public Delete<OBJECT_TYPE> and(ExpressionQuery expressionQuery) {
        whereQuery.and(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public int execute() throws Exception {
        return obtain.getInt(this);
    }
}
