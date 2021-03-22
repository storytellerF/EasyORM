package com.storyteller_f.sql_query.query.result;

public class Item {
    protected String getType() {
        return type;
    }
    protected void setType(String type) {
        this.type = type;
    }
    protected Object getObject() {
        return object;
    }
    protected void setObject(Object object) {
        this.object = object;
    }
    private String type;
    private Object object;
}
