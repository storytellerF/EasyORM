package obtain;

import annotation.Children;
import annotation.Column;
import annotation.Convert;
import org.apache.commons.text.WordUtils;
import query.query.ExecutableQuery;
import query.result.Result;
import util.ORMUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.UnexpectedException;

public abstract class Obtain {
    public abstract Result getResult(ExecutableQuery<?> executableQuery) throws Exception;
    public abstract int getInt(ExecutableQuery<?> executableQuery) throws Exception;
    void provide(Class<?> returnType, Fields fields, DatabaseTable databaseTable, Result result) throws NoSuchFieldException, NoSuchMethodException {
        int columnCount= result.getColumnCount();
        /*
         */
        for (int i = 0; i < columnCount; i++) {

            String type = databaseTable.getType(i);
            Class<?> typeClass = ORMUtil.getClassByType(type);
            int hasColumnAnnotation = databaseTable.getAnnotation(i);
            if (hasColumnAnnotation == -1) {
                /*
                 当前列的名字没有通过@Column 注解特殊设置，一般情况下字段名和列名是相同的，如果不相同，那也不能够进行处理
                 不做空指针判断，因为找不到就会抛出异常
                 */
                String columnName = databaseTable.getName(i);
                try{
                    Field field = returnType.getDeclaredField(columnName);
                    if (field.isAnnotationPresent(Convert.class)) {
                        result.add(field, i);
                        result.add(typeClass, i);
                    } else {
                        //使用方法，这样不需要设置权限，当然默认setter是共有的，使用方法好处更多
                        String methodName = "set" + WordUtils.capitalize(columnName);
                        Method method = returnType.getDeclaredMethod(methodName, typeClass);
                        result.add(method, i);
                        result.add(typeClass,i);
                    }
                }catch (NoSuchFieldException e){
//                    System.out.println(e.getMessage());
                    //只能是子对象中的，不然无法处理
                    Field fieldInChildren = getFieldInChildren(columnName, returnType);
                    result.add(fieldInChildren,i);
                }


            } else {// 当前列通过@Column 注解特殊设置过，所以可能字段名与列名不同，最好使用字段
                Field fieldWith = fields.getField(hasColumnAnnotation);
                result.add(typeClass, i);
                result.add(fieldWith, i);
            }
        }
    }

    protected Field getFieldInChildren(String columnName, Class<?> returnType){
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
     * @param executableQuery 可执行语句
     * @return 返回类型
     * @throws UnexpectedException 返回类型为整形
     */
    Class<?> getReturnType(ExecutableQuery<?> executableQuery) throws UnexpectedException {
        Class<?> returnType = executableQuery.getReturnType();
//        System.out.println("return:" + returnType.getName());
        if (returnType == Integer.class) {
            // 出现错误
            throw new UnexpectedException("返回值是整形，却做的select 请求");
        }
        return returnType;
    }
    Fields getFields(Class<?> returnType) {
        Fields fields=new Fields();
        Field[] allFields = returnType.getDeclaredFields();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                fields.add(column.name(),field);
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
    public DatabaseTable getDatabaseTable(Columns resultSetMetaData, int columnCount, Fields fields) {

        DatabaseTable databaseTable = new DatabaseTable(columnCount);
        for (int i1 = 0; i1 < columnCount; i1++) {
            /*
             * 获取到列类型和列名
             */
            String columnLabel = resultSetMetaData.getName(i1);
            String columnTypeName = resultSetMetaData.getType(i1);
            databaseTable.add(i1, columnLabel, columnTypeName);
            int j;
            for (j = 0; j < fields.getCount(); j++) {
                if (fields.getName(j).equals(columnLabel)) {// 找到了和当前列对应的字段
                    break;
                }
            }
            if (j != fields.getCount()) {// 找到了字段，并且位置就是j
                databaseTable.add(i1,j);
            }
        }
        return databaseTable;
    }
}
