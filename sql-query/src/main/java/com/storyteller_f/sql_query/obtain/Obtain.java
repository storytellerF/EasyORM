package com.storyteller_f.sql_query.obtain;

import com.storyteller_f.sql_query.annotation.Children;
import com.storyteller_f.sql_query.annotation.Column;
import com.storyteller_f.sql_query.annotation.Convert;
import com.storyteller_f.sql_query.query.*;
import com.storyteller_f.sql_query.util.ReflectUtil;
import org.apache.commons.text.WordUtils;
import com.storyteller_f.sql_query.query.query.ExecutableQuery;
import com.storyteller_f.sql_query.query.result.Result;
import com.storyteller_f.sql_query.util.ORMUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.UnexpectedException;
import java.util.List;

public abstract class Obtain {

    public <T> Select<T> getSelect() {
        return new Select<>(this);
    }

    public <T> Select<T> getSelect(Class<T> tClass) {
        return new Select<T>(this).select(tClass);
    }

    public <T> Update<T> getUpdate() {
        return new Update<>(this);
    }

    public <T> Insert<T> getInsert() {
        return new Insert<>(this);
    }

    public <T> Delete<T> getDelete() {
        return new Delete<>(this);
    }

    /**
     * 继承此方法是，需要调用 provideAccessPoint
     *
     * @param executableQuery
     * @return 返回平台平台无关的结果
     * @throws Exception
     */
    public abstract Result getResult(Select<?> executableQuery) throws Exception;

    public abstract int getInt(ExecutableQuery<?> executableQuery) throws Exception;

    /**
     * 获取可以操作当前字段的方法，可能时Method ，也可能时Field 本身
     * 如果能够获得方法，还是应该获得方法
     * 如果获取不到，那就只能使用Field
     *
     * @param returnType    返回类型，进对于select 有效
     * @param columnFields  存储字段信息，带有@Column 的字段
     * @param databaseTable 数据库表主要结构
     * @param result        存储执行的结果
     * @throws NoSuchMethodException
     */
    protected void provideAccessPoint(Class<?> returnType, ColumnFields columnFields, DatabaseTable databaseTable, Result result) throws NoSuchMethodException, NoSuchFieldException {
        int columnCount = result.getColumnCount();
        /*
         */
        for (int i = 0; i < columnCount; i++) {
            String type = databaseTable.getType(i);
            Class<?> typeClass = ORMUtil.getClassByType(type);
            String columnNameInDatabase = databaseTable.getName(i);
            System.out.println(i + " " + columnNameInDatabase);
            String name;
            Field field = columnFields.getField(columnNameInDatabase);
            if (field == null) {
                name=columnNameInDatabase;
                /*
                 当前列的名字没有通过@Column 注解特殊设置，这种情况下要求字段名和列名是相同的，如果不相同，那也不能够进行处理
                 不需要做空指针判断，因为找不到就会抛出异常
                 */
            } else {// 当前列通过@Column 注解特殊设置过，所以可能字段名与列名不同，最好使用字段
                name= field.getName();
                //但还是获取一下方法试试
            }
            getAccessPointWhenNoAnnotation(returnType, result, i, typeClass, name);
        }
    }

    /**
     * 没有使用@Column 注解
     *
     * @param returnType
     * @param result
     * @param i
     * @param typeClass
     * @param columnName
     */
    private void getAccessPointWhenNoAnnotation(Class<?> returnType, Result result, int i, Class<?> typeClass, String columnName) throws NoSuchFieldException {
        result.add(typeClass, i);
        //todo 首先获取方法，如果获取不到，就获取字段
        String methodName = "set" + WordUtils.capitalize(columnName);
        MethodWithPath methodInChildren = getMethodInChildren(methodName, returnType, "");
        if (methodInChildren!=null){//方法找到了
            result.add(methodInChildren,i);
        }else {//方法没有找到
            FieldWithPath field = getFieldInChildren(columnName, returnType, "");//todo 字段可能使用了转换函数
            if (field==null){
                throw new NoSuchFieldException("找不到相应的字段");
            }
            result.add(field,i);
        }

    }


    /**
     * 在当前类中获取不到，只能到标有@Children 的字段中获取
     *
     * @param fieldName  要获得的字段名
     * @param returnType 用来获取所有的字段
     * @param path
     * @return 获得的字段，如果找不到，返回null
     */
    protected FieldWithPath getFieldInChildren(String fieldName, Class<?> returnType, String path) {
        List<Field> declaredFields = ReflectUtil.getAllField(returnType);
        for (Field declaredField : declaredFields) {
            if (fieldName.equals(declaredField.getName())) {
                return new FieldWithPath(declaredField, path);
            }
            if (declaredField.isAnnotationPresent(Children.class)) {
                FieldWithPath fieldInChildren = getFieldInChildren(fieldName, declaredField.getType(), path + "/" + declaredField.getName());
                if (fieldInChildren != null)
                    return fieldInChildren;
            }
        }
        return null;
    }

    protected MethodWithPath getMethodInChildren(String methodName, Class<?> c, String path) {
        List<Method> methods = ReflectUtil.getAllMethod(c);
        for (Method declaredField : methods) {
            if (methodName.equals(declaredField.getName())) {
                return new MethodWithPath(declaredField, path);
            }
        }
        //TODO 查找所有字段，继续向下查找方法
        List<Field> allField = ReflectUtil.getAllField(c);
        for (Field field : allField) {
            if (field.isAnnotationPresent(Children.class)) {
                MethodWithPath methodInChildren = getMethodInChildren(methodName, field.getType(), path + "/" + field.getName());
                if (methodInChildren != null) return methodInChildren;
            }
        }
        return null;
    }

    /**
     * 带有类型检查，如果出现错误，经会抛出异常
     *
     * @param executableQuery 可执行语句
     * @return 返回类型
     * @throws UnexpectedException 返回类型为整形
     */
    protected Class<?> getReturnType(Select<?> executableQuery) throws UnexpectedException {
        Class<?> returnType = executableQuery.getReturnType();
//        System.out.println("return:" + returnType.getName());
        if (returnType == Integer.class) {
            // 出现错误
            throw new UnexpectedException("返回值是整形，却做的Select 请求");
        }
        return returnType;
    }

    /**
     * 获取带有@Column 注解的字段
     *
     * @param returnType 用来获取所有字段
     * @return
     */
    protected ColumnFields getFields(Class<?> returnType, int count) {
        Field[] allFields = returnType.getDeclaredFields();
        ColumnFields columnFields = new ColumnFields(count);
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Children.class)) {
                columnFields.add(getFields(field.getType(), field.getClass().getDeclaredFields().length));
            }
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columnFields.add(column.name(), field);
            }
        }
        return columnFields;
    }

    /*
     * *遍历所有数据库段列，
     * 并获取对应的字段，
     * 因为在模型类中字段的顺序，并不是和数据库返回的数据顺序一定符合
     * *比如自己手动修改语句
     */
    public DatabaseTable getDatabaseTable(DatabaseColumnsInfo databaseColumnsInfo) {
        return new DatabaseTable(databaseColumnsInfo);
    }
}
