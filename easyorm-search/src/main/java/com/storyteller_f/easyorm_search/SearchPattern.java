package com.storyteller_f.easyorm_search;

import com.storyteller_f.sql_query.exception.UnexpectedTypeException;
import com.storyteller_f.sql_query.query.expression.LikeExpression;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;

import java.lang.reflect.Field;
import java.rmi.UnexpectedException;
import java.util.ArrayList;

public class SearchPattern {
    public ExpressionQuery[] parse() throws UnexpectedTypeException, IllegalAccessException, UnexpectedException {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        ArrayList<ExpressionQuery> expressionQueries = new ArrayList<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            Class<?> type = declaredField.getType();
            if (type.isPrimitive()) {
                throw new UnexpectedTypeException("禁止使用Primitive int double float bool 等,应该使用Integer Double Float Boolean等");
            }
            boolean a = declaredField.isAccessible();
            declaredField.setAccessible(true);
            Object o = declaredField.get(this);
            declaredField.setAccessible(a);
            if (o == null) {
                continue;
            }
            if ("java.lang.String".equals(type.getName())) {
                if (o instanceof String) {
                    if (stringNotEmpty((String) o)) {
                        expressionQueries.add(new LikeExpression<>(name, o));
                    }
                } else {
                    throw new UnexpectedException("不太可能出现");
                }
            } else {
                expressionQueries.add(new LikeExpression<>(name, o));
            }
        }

        ExpressionQuery[] array = new ExpressionQuery[expressionQueries.size()];
        return expressionQueries.toArray(array);
    }

    public boolean stringNotEmpty(String str) {
        if (str == null) {
            return false;
        }
        return str.trim().length() != 0;
    }
}
