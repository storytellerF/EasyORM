package com.storyteller_f.sql_query.query.expression;

import com.storyteller_f.sql_query.query.query.ExecutableQuery;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;

public class InExpression<T> extends ExpressionQuery {
    private final ExecutableQuery<?> executableQuery;
    private final String fieldName;

    public InExpression(ExecutableQuery<?> executableQuery, String fieldName) {
        this.executableQuery = executableQuery;
        this.fieldName = fieldName;
    }

    @Override
    public Object clone() {
        return new InExpression<T>(executableQuery, fieldName);
    }

    @Override
    public String parse(boolean safe) throws Exception {
        String string = next != null ? next.parse(safe) : "";
        return fieldName + " in (" + executableQuery.parse(safe) + string + ")";
    }
}
