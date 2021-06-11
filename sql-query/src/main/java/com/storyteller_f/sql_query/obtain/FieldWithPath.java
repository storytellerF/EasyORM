package com.storyteller_f.sql_query.obtain;

import java.lang.reflect.Field;

public class FieldWithPath {
    public Field field;
    /**
     * 示例:/children1/children2
     * 如果在当前的类中就是空字符串
     */
    public String path;

    public FieldWithPath(Field declaredField, String path) {
        this.field=declaredField;
        this.path=path;
    }
}
