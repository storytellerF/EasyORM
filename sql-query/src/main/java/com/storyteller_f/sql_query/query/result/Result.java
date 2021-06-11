package com.storyteller_f.sql_query.query.result;

import com.storyteller_f.sql_query.obtain.FieldWithPath;
import com.storyteller_f.sql_query.obtain.MethodWithPath;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class Result implements Iterator<Object[]> {
    /**
     * 从数据库中获取的所有的值
     */
    private final ArrayList<Object[]> data = new ArrayList<>();
    /**
     * 存储一个Field 或者一个Method
     */
    private Object[] objects;
    private Class<?>[] typeClasses;
    private int index = -1;

    public int getCount() {
        return data.size();
    }

    public void setCount(int count) {
        objects = new Object[count];
        typeClasses = new Class<?>[count];
    }

    public void add(Class<?> typeClass, int i) {
        typeClasses[i] = typeClass;
    }

    public Class<?> getType(int i) {
        return typeClasses[i];
    }

    /**
     * 检查定义了类型和返回的类型是否一致
     * 如果是个方法，就比较参数
     *
     * @param i 位置
     * @return 如果一致，返回true
     */
    public boolean typeCoincide(int i) {
        Object object = objects[i];
        if (object instanceof Field) {
            return ((Field) object).getType().equals(typeClasses[i]);
        } else {
            Class<?> parameterType = ((Method) object).getParameterTypes()[0];
            return parameterType.equals(typeClasses[i]);
        }
    }

    public int getColumnCount() {
        return objects.length;
    }

    public void add(MethodWithPath method, int index) {
        objects[index] = method;
    }

    public void add(FieldWithPath field, int index) {
        objects[index] = field;
    }

    public MethodWithPath getMethod(int index) {
        Object object = objects[index];
        if (object instanceof MethodWithPath) {
            return (MethodWithPath) object;
        } else {
            return null;
        }
    }

    public FieldWithPath getField(int index) {
        Object object = objects[index];
        if (object instanceof FieldWithPath) {
            return (FieldWithPath) object;
        } else {
            return null;
        }
    }

    @Override
    public boolean hasNext() {
        return index < data.size() - 1 && data.size() > 0;
    }

    public ArrayList<Object[]> getData() {
        return data;
    }

    @Override
    public Object[] next() {
        if (index < data.size()) {
            index++;
        }
        return data.get(index);
    }
}
