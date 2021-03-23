package com.storyteller_f.sql_query.obtain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class Fields {

    HashMap<String,Field> value;

    public Fields(int count) {
        value=new HashMap<>(count);
    }

    public void add(String name, Field field) {
        value.put(name,field);
    }

    public Field getField(String name) {
        return value.get(name);
    }

    public void add(Fields fields) {
        fields.value.forEach((s, field) -> {
            value.put(s,field);
        });
    }
}
