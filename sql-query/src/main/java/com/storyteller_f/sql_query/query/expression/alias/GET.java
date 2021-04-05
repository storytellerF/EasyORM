package com.storyteller_f.sql_query.query.expression.alias;

import com.storyteller_f.sql_query.query.GetName;
import com.storyteller_f.sql_query.query.expression.GreaterThanAndEqualExpression;

public class GET<T> extends GreaterThanAndEqualExpression<T> {
    public GET(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public GET(Class<?> tableClass, GetName<String> fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public GET(String fieldName, T value) {
        super(fieldName, value);
    }

    public GET(GetName<String> fieldName, T value) {
        super(fieldName, value);
    }
}
