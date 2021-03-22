package com.storyteller_f.sql_query.obtain;

import com.storyteller_f.sql_query.annotation.Children;
import com.storyteller_f.sql_query.annotation.Column;
import com.storyteller_f.sql_query.annotation.Convert;
import com.storyteller_f.sql_query.query.*;
import org.apache.commons.text.WordUtils;
import com.storyteller_f.sql_query.query.query.ExecutableQuery;
import com.storyteller_f.sql_query.query.result.Result;
import com.storyteller_f.sql_query.util.ORMUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.UnexpectedException;

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

    public <T>Delete<T> getDelete(){
        return new Delete<>(this);
    }

    public abstract Result getResult(ExecutableQuery<?> executableQuery) throws Exception;

    public abstract int getInt(ExecutableQuery<?> executableQuery) throws Exception;

    /**
     * 获取可以操作当前字段的方法，可能时Method ，也可能时Field 本身
     * 如果能够获得方法，还是应该获得方法
     * 如果获取不到，那就只能使用Field
     *
     * @param returnType    返回类型，进对于select 有效
     * @param fields        存储字段信息
     * @param databaseTable 数据库表主要结构
     * @param result        存储执行的结果
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     */
    protected void provide(Class<?> returnType, Fields fields, DatabaseTable databaseTable, Result result) throws NoSuchFieldException, NoSuchMethodException {
        int columnCount = result.getColumnCount();
        /*
         */
        for (int i = 0; i < columnCount; i++) {

            String type = databaseTable.getType(i);
            Class<?> typeClass = ORMUtil.getClassByType(type);
            int hasColumnAnnotation = databaseTable.getAnnotation(i);
            String columnName = databaseTable.getName(i);
            if (hasColumnAnnotation == -1) {
                /*
                 当前列的名字没有通过@Column 注解特殊设置，一般情况下字段名和列名是相同的，如果不相同，那也不能够进行处理
                 不做空指针判断，因为找不到就会抛出异常
                 */
                /*
                 * 数据库中存储的列名
                 */
                try {
                    Field field = returnType.getDeclaredField(columnName);
                    if (field.isAnnotationPresent(Convert.class)) {//使用了转换函数，不在这里获取，交由其他布局处理
                        result.add(field, i);
                        result.add(typeClass, i);
                    } else {
                        //使用方法，这样不需要设置权限，当然默认setter是共有的，使用方法好处更多
                        String methodName = "set" + WordUtils.capitalize(columnName);
                        Method method = returnType.getDeclaredMethod(methodName, typeClass);
                        result.add(method, i);
                        result.add(typeClass, i);
                    }
                } catch (NoSuchFieldException e) {
//                    System.out.println(e.getMessage());
                    //只能是子对象中的，不然无法处理
                    Field fieldInChildren = getFieldInChildren(columnName, returnType);
                    result.add(fieldInChildren, i);
                }


            } else {// 当前列通过@Column 注解特殊设置过，所以可能字段名与列名不同，最好使用字段
                //但还是获取一下方法试试
                try {
                    //使用方法，这样不需要设置权限，当然默认setter是共有的，使用方法好处更多
                    String methodName = "set" + WordUtils.capitalize(columnName);
                    Method method = returnType.getDeclaredMethod(methodName, typeClass);
                    result.add(method, i);
                    result.add(typeClass, i);
                } catch (Exception exception) {
                    Field fieldWith = fields.getField(hasColumnAnnotation);
                    result.add(typeClass, i);
                    result.add(fieldWith, i);

                }
            }
        }
    }

    /**
     * 在当前类中获取不到，只能到标有@Children 的字段中获取
     *
     * @param columnName 要获得的列名
     * @param returnType 用来获取所有的字段
     * @return 获得的字段，如果找不到，返回null
     */
    protected Field getFieldInChildren(String columnName, Class<?> returnType) {
        Field[] declaredFields = returnType.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Children.class)) {
//                System.out.println(declaredField.getName()+" "+columnName);
                try {
                    return declaredField.getType().getDeclaredField(columnName);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
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
    protected Class<?> getReturnType(ExecutableQuery<?> executableQuery) throws UnexpectedException {
        Class<?> returnType = executableQuery.getReturnType();
//        System.out.println("return:" + returnType.getName());
        if (returnType == Integer.class) {
            // 出现错误
            throw new UnexpectedException("返回值是整形，却做的Select 请求");
        }
        return returnType;
    }

    /**
     * 获取字段的真实名称
     *
     * @param returnType 用来获取所有字段
     * @return
     */
    protected Fields getFields(Class<?> returnType) {
        Fields fields = new Fields();
        Field[] allFields = returnType.getDeclaredFields();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                fields.add(column.name(), field);
            }
        }
        return fields;
    }

    /*
     * *遍历所有数据库段列，
     * 并获取对应的字段，
     * 因为在模型类中字段的顺序，并不是和数据库返回的数据顺序一定符合
     * *比如自己手动修改语句
     */
    public DatabaseTable getDatabaseTable(Columns columns, int columnCount, Fields fields) {

        DatabaseTable databaseTable = new DatabaseTable(columnCount);
        for (int i1 = 0; i1 < columnCount; i1++) {
            /*
             * 获取到列类型和列名
             */
            String columnLabel = columns.getName(i1);
            String columnTypeName = columns.getType(i1);
            databaseTable.add(i1, columnLabel, columnTypeName);
            int j;
            for (j = 0; j < fields.getCount(); j++) {
                if (fields.getName(j).equals(columnLabel)) {// 找到了和当前列对应的字段
                    break;
                }
            }
            if (j != fields.getCount()) {// 找到了字段，并且位置就是j
                databaseTable.add(i1, j);
            }
        }
        return databaseTable;
    }
}
