package com.storyteller_f.sql_query.obtain;

import java.lang.reflect.Method;

public class MethodWithPath {
    public Method method;
    public String path;

    public MethodWithPath(Method declaredField, String path) {
        this.method=declaredField;
        this.path=path;
    }
}
