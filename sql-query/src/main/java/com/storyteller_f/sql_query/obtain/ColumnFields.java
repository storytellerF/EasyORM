package com.storyteller_f.sql_query.obtain;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * 存储带有@Column 注解的字段
 */
public class ColumnFields {

    HashMap<String,Field> value;

    public ColumnFields(int count) {
        value=new HashMap<>(count);
    }

    public void add(String name, Field field) {
        value.put(name,field);
    }

    public Field getField(String name) {
        return value.get(name);
    }

    public void add(ColumnFields columnFields) {
        columnFields.value.forEach((s, field) -> {
            value.put(s,field);
        });
    }
}
