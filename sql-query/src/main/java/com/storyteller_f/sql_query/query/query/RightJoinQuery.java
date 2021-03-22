package com.storyteller_f.sql_query.query.query;

public class RightJoinQuery extends JoinQuery{
    public RightJoinQuery(Class<?> tableClass) {
        super(tableClass);
    }

    @Override
    public String parse(boolean safe) throws Exception {
        return "right"+ super.parse(safe);
    }
}
