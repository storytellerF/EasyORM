package com.storyteller_f.sql_query.query;

public class ForeignKey {
    private Class<?> tableClass;
    private String column;
    public ForeignKey(Class<?> tableClass, String column) {
        super();
        this.tableClass = tableClass;
        this.column = column;
    }
    protected Class<?> getTableClass() {
        return tableClass;
    }
    protected void setTableClass(Class<?> tableClass) {
        this.tableClass = tableClass;
    }
    protected String getColumn() {
        return column;
    }
    protected void setColumn(String column) {
        this.column = column;
    }
}
