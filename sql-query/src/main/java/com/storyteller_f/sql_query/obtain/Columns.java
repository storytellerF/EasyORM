package com.storyteller_f.sql_query.obtain;

public class Columns {
    /**
     * 从数据库中获得真实的列名
     */
    private final String[] name;
    private final String[] type;

    public Columns(int count) {
        name=new String[count];
        type=new String[count];
    }

    public void add(int index,String name, String type) {
        this.name[index]=name;
        this.type[index]=type;
    }

    /**
     * 数据库的真实列名，没进行任何处理
     * @param index
     * @return
     */
    public String getName(int index) {
        return name[index];
    }

    public String getType(int index) {
        return type[index];
    }

    public int getCount() {
        return name.length;
    }
}
