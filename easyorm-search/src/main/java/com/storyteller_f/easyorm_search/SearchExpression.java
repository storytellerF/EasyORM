package com.storyteller_f.easyorm_search;

import com.storyteller_f.sql_query.query.expression.EqualExpression;

import java.lang.reflect.Field;

public class SearchExpression {
    public EqualExpression<?>[] parse(SearchPattern searchPattern){
        Field[] declaredFields = searchPattern.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getName());
        }
        return null;
    }
}
