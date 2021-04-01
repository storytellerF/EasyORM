package com.storyteller_f.sql_query.query.query;

import com.storyteller_f.sql_query.query.Executor;
import com.storyteller_f.sql_query.query.Query;
import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.util.ORMUtil;

public abstract class ExecutableQuery<CHILD_TYPE extends ExecutableQuery<CHILD_TYPE>> extends Executor implements Query {
    protected TableQuery tableQuery;
    private Class<?> returnType;

    public ExecutableQuery(Obtain obtain) {
        super(obtain);
        tableQuery = new TableQuery();
    }

    /**
     * *继承此类的的类重载此方法时最好调用super.parse();
     */
    @Override
    public String parse(boolean safe) throws Exception {
        if (tableQuery.count() == 0) {
            table(getReturnType());
        }
        return null;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    protected void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public abstract ExpressionQuery getExpressionQuery();

    @SuppressWarnings("unchecked")
    public CHILD_TYPE table(String name) {
        tableQuery.table(name);
        return (CHILD_TYPE) this;
    }

    public CHILD_TYPE table(Class<?> tableClass) {
        String name = ORMUtil.getTrueTableName(tableClass);
        return table(name);
    }

    @SuppressWarnings("unchecked")
    public CHILD_TYPE table(String name, String alias) {
        tableQuery.table(name, alias);
        return (CHILD_TYPE) this;
    }

    public CHILD_TYPE table(Class<?> tableClass, String alias) {
        String name;
        name = ORMUtil.getTrueTableName(tableClass);
        return table(name, alias);
    }

}
