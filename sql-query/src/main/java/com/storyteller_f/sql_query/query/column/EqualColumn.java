package com.storyteller_f.sql_query.query.column;

import com.storyteller_f.sql_query.query.GetName;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;
import com.storyteller.util.ORMUtil;

public class EqualColumn extends ExpressionQuery {
    private final Class<?> tableLeftClass;
    private final Class<?> tableRightClass;
    private final String columnLeft;
    private final String columnRight;

    public EqualColumn(Class<?> tableLeftClass, String columnLeft, Class<?> tableRightClass, String columnRight) {
        super();
        this.tableLeftClass = tableLeftClass;
        this.tableRightClass = tableRightClass;
        this.columnLeft = columnLeft;
        this.columnRight = columnRight;
    }

    public EqualColumn(Class<?> tableLeftClass, GetName<String> columnLeft, Class<?> tableRightClass, GetName<String> columnRight) {
        super();
        this.tableLeftClass = tableLeftClass;
        this.tableRightClass = tableRightClass;
        this.columnLeft = columnLeft.get();
        this.columnRight = columnRight.get();
    }

    @Override
    public Object clone() {
        return new EqualColumn(tableLeftClass, columnLeft, tableRightClass, columnRight);
    }

    @Override
    public String parse(boolean safe) throws Exception {
        String left = ORMUtil.getTrueSqlColumn(tableLeftClass, columnLeft, tableMap);
        String right = ORMUtil.getTrueSqlColumn(tableRightClass, columnRight, tableMap);
        return left + " = " + right + (next != null ? " & " + next.parse(safe) : "");
    }
}
