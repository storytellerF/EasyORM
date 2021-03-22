package com.storyteller_f.sql_query.query.result;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Result implements Iterator<Object[]> {

    private Object[] objects;
    private Class<?>[] typeClasses;
    private int index = -1;
    private HashMap<Class<?>,Result> childMap=new HashMap<>();
    private final ArrayList<Object[]> data = new ArrayList<>();

    public int getCount() {
        return data.size();
    }

    public void add(Class<?> typeClass, int i) {
        typeClasses[i] = typeClass;
    }

    public Class<?> getType(int i) {
        return typeClasses[i];
    }

    /**
     * 检查定义了类型和返回的类型是否一致
     *
     * @param i 位置
     * @return 如果一致，返回true
     * @throws UnexpectedException 当前对象不是field
     */
    public boolean typeCoincide(int i) throws UnexpectedException {
        Object object = objects[i];
        if (object instanceof Field) {
            return ((Field) object).getType().equals(typeClasses[i]);
        } else {
            throw new UnexpectedException("当前对象不是Field");
        }

    }

    public int getColumnCount() {
        return objects.length;
    }

    public void setCount(int count) {
        objects = new Object[count];
        typeClasses = new Class<?>[count];
    }

    public void add(Method method, int index) {
        objects[index] = method;
    }

    public void add(Field field, int index) {
        objects[index] = field;
    }

    public Method getMethod(int index) {
        Object object = objects[index];
        if (object instanceof Method) {
            return (Method) object;
        } else {
            return null;
        }
    }

    public Field getField(int index) {
        Object object = objects[index];
        if (object instanceof Field) {
            return (Field) object;
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
