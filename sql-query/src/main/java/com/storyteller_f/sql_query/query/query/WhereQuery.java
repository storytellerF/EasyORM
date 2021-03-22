package com.storyteller_f.sql_query.query.query;

import java.util.HashMap;

public class WhereQuery extends AbstractExpressionList{
    public void and(ExpressionQuery expressionQuery, HashMap<String,String> tableMap) {
        next(expressionQuery, tableMap);
    }
    @Override
    public String parse(boolean safe) throws Exception {
        if (headerPointer ==null) {
            return "";
        }
        return "where "+ super.parse(safe);
    }
    public interface WhenWhere<T>{
        ExpressionQuery on (T t);
    }
}
