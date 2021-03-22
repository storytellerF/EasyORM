package com.storyteller_f.sql_query.query.query;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import com.storyteller_f.sql_query.annotation.Children;
import com.storyteller_f.sql_query.annotation.NoQuery;
import com.storyteller_f.sql_query.query.Query;
import com.storyteller_f.sql_query.util.ORMUtil;

public class SelectQuery implements Query{
    private HashMap<String, String> tableMap;
    public void setTableMap(HashMap<String, String> tableMap) {
        this.tableMap = tableMap;
    }
    private Class<?> tableClass;
    private Class<?> trueTable;
    private Field[] fields;
    public void select(Class<?> tableClass)  {
        this.tableClass=tableClass;
        if (tableClass!=null) {
            fields=tableClass.getDeclaredFields();
        }
    }
    private final HashSet<String> columnNames=new HashSet<>();
    private final HashSet<String> columnNamesInChildren=new HashSet<>();
    @Override
    public String parse(boolean safe) throws Exception {
        if (fields==null) {
            return "select *";
        }
        columnNamesInChildren.clear();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("select ");
        //全部遍历一遍
        for (Field field : fields) {
            if (field.isAnnotationPresent(NoQuery.class)) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.isAnnotationPresent(Children.class)) {
                handleChildren(field);
                continue;
            }
            String columnName;
            if (tableMap.isEmpty() || tableMap.size() == 1) {
                columnName = ORMUtil.getColumn(field);
            } else {
                columnName = ORMUtil.getTrueSqlColumn(trueTable != null ? trueTable : tableClass, field.getName(), tableMap);
            }
            columnNames.add(columnName);
        }
        int index=0;
        //添加正常字段
        int count=columnNames.size();
        for (String columnName : columnNames) {
            if (columnNamesInChildren.contains(columnName)) {
                throw new Exception("已经声明的字段，不可以再次出现在子字段中");
            }
            stringBuilder.append(columnName);
            if (index != count - 1) {
                stringBuilder.append(",");
            }
            index++;
        }

        index=0;
        count=columnNamesInChildren.size();
        //添加填写在子字段中的字段
        if (!columnNamesInChildren.isEmpty()) {
            stringBuilder.append(",");
            for (String string :
                    columnNamesInChildren) {
                stringBuilder.append(string);
                if (index!=count-1) {
                    stringBuilder.append(",");
                }
                index++;
            }
        }
        stringBuilder.append(" from");
        return stringBuilder.toString();
    }

    private void handleChildren(Field field) throws Exception {
        Class<?> aClass = field.getType();
        String simpleName = aClass.getSimpleName();
        if (simpleName.equals("int") || simpleName.equals("String") || simpleName.equals("bool") || simpleName.equals("float")
                || simpleName.equals("Array")) {
            throw new Exception("不可以用@Children 修饰基本数据类型");
        }
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field f : declaredFields) {
            if (f.isAnnotationPresent(NoQuery.class)) {
                continue;
            }
            String columnName;
            if (tableMap.isEmpty()||tableMap.size()==1) {
                columnName=ORMUtil.getColumn(f);
            }else {
                columnName=ORMUtil.getTrueSqlColumn(trueTable!=null?trueTable:tableClass, f.getName(), tableMap);
            }
            if (columnNamesInChildren.contains(columnName)) {
                throw new Exception("不可以包含重复字段"+columnName);
            }
            columnNamesInChildren.add(columnName);
        }

    }

    /**
     * *当查找的表不是真实的数据库中存储的表时，在需要给列指定表名时需要多传递一个参数
     * @param tableClass 可能真实的名字，也可能仅仅只是完整表的一部分
     * @param trueTable 真正的名字一般是from 后面表名
     */
    public void select(Class<?> tableClass, Class<?> trueTable) {
        select(tableClass);
        this.trueTable=trueTable;
    }
}
