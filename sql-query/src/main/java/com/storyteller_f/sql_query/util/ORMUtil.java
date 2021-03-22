package com.storyteller_f.sql_query.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.UnexpectedException;
import java.util.Date;
import java.util.HashMap;

import com.storyteller_f.sql_query.annotation.Column;
import com.storyteller_f.sql_query.annotation.Convert;
import com.storyteller_f.sql_query.annotation.Table;
import com.storyteller_f.sql_query.annotation.type.string.BinaryColumn;
import com.storyteller_f.sql_query.annotation.type.string.BlobColumn;
import com.storyteller_f.sql_query.annotation.type.string.CharColumn;
import com.storyteller_f.sql_query.annotation.type.string.EnumColumn;
import com.storyteller_f.sql_query.annotation.type.string.TextColumn;
import com.storyteller_f.sql_query.annotation.type.string.VarbinaryColumn;
import com.storyteller_f.sql_query.annotation.type.string.VarcharColumn;

public class ORMUtil {
    public static String getTrueTableName(Class<?> tableClass) {
        String name;
        if (tableClass.isAnnotationPresent(Table.class)) {
            name = tableClass.getAnnotation(Table.class).name();
        } else {
            name = tableClass.getSimpleName();
        }
        return name;
    }

    /**
     * *根据Annotation 获取数据库中的类
     *
     * @param annotation 注解
     * @return 返回数据库类型
     * @throws UnexpectedException 不支持的类型
     */
    public static String getType(Annotation annotation) throws UnexpectedException {
        String annotationName = annotation.annotationType().getName();
//		System.out.println(nameString);
        switch (annotationName) {
            case "com.storyteller_f.sql_query.annotation.type.exact.IntColumn":
                return "int";
            case "com.storyteller_f.sql_query.annotation.type.exact.BigIntColumn":
                return "bigint";
            case "com.storyteller_f.sql_query.annotation.type.exact.MediumIntColumn":
                return "mediumint";
            case "com.storyteller_f.sql_query.annotation.type.exact.TinyIntColumn":
                return "tinyint";
            case "com.storyteller_f.sql_query.annotation.type.exact.SmallIntColumn":
                return "smallint";
            case "com.storyteller_f.sql_query.annotation.type.exact.DecimalColumn":
                return "decimal";
            case "com.storyteller_f.sql_query.annotation.type.exact.NumericColumn":
                return "numeric";
            case "com.storyteller_f.sql_query.annotation.type.string.VarcharColumn":
                return "varchar(" + ((VarcharColumn) annotation).length() + ")";
            case "com.storyteller_f.sql_query.annotation.type.string.BinaryColumn":
                return "binary(" + ((BinaryColumn) annotation).length() + ")";
            case "com.storyteller_f.sql_query.annotation.type.string.CharColumn":
                return "char(" + ((CharColumn) annotation).length() + ")";
            case "com.storyteller_f.sql_query.annotation.type.string.BlobColumn":
                return "blog(" + ((BlobColumn) annotation).length() + ")";
            case "com.storyteller_f.sql_query.annotation.type.string.VarbinaryColumn":
                return "varbinary(" + ((VarbinaryColumn) annotation).length() + ")";
            case "com.storyteller_f.sql_query.annotation.type.string.TextColumn":
                return "text(" + ((TextColumn) annotation).length() + ")";
            case "com.storyteller_f.sql_query.annotation.type.string.EnumColumn":
                EnumColumn enumColumn = (EnumColumn) annotation;
                if (enumColumn.type()) {
                    return "enum(" + (getEnumList(enumColumn)) + ")";
                } else {
                    return null;
                }

            default:
                System.out.println(annotationName);
                throw new UnexpectedException("未支持的类型:" + annotationName);
        }

    }

    private static String getEnumList(EnumColumn enumColumn) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] array = enumColumn.value();
        for (String s : array) {
            stringBuilder.append("'").append(s).append("',");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static Class<?> getClassByType(String type) throws IllegalArgumentException {
        switch (type) {
            case "VARCHAR":
            case "CHAR":
            case "TEXT":
            case "BIT":
            case "LONGTEXT":
            case "TINYTEXT":
            case "MEDIUMTEXT":
                return String.class;
//		case "BINARY":
//		case "VARBINARY":
//		case "BLOG":
//		case "LONGBLOG":
//		case "TINYBLOG":
//		case "MEDIUMBLOG":
//		case "ENUM":
//		case "SET":

            case "INT":
            case "BINGINT":
            case "SMALLINT":
            case "MEDIUMINT":
            case "TINYINT":
            case "SERIAL":
                return int.class;
            case "DATE":
            case "DATETIME":
                return Date.class;
            case "REAL":
            case "DECIMAL":
            case "DOUBLE":
                return double.class;
            case "FLOAT":
                return float.class;
            case "BOOLEAN":
                return boolean.class;
            default:
                throw new IllegalArgumentException("type can't be tacked:" + type);
        }
    }

    public static <T> Object getValue(T t, Class<?> clasx, Field field) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        boolean a = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(t);
        field.setAccessible(a);
        if (field.isAnnotationPresent(Convert.class)) {
            Convert convert = field.getAnnotation(Convert.class);
            String methodName;
            if (convert.reverse().length() == 0) {
                methodName = reverseMethod(convert.name());
            } else {
                methodName = convert.reverse();
            }
            Method method = clasx.getDeclaredMethod(methodName, field.getType());
            System.out.println("转换前：" + value + " :" + value.getClass());
            value = method.invoke(t, value);
            System.out.println("转化后：" + value + " :" + value.getClass());
        }
        return value;
    }

    public static String reverseMethod(String orign) {
        char[] chars = new char[orign.length()];
        for (int i = 0; i < orign.length(); i++) {
            chars[i] = orign.charAt(orign.length() - i - 1);
        }
        return new String(chars);
    }

    public static String getColumn(Field field) {
        String name;
        if (field.isAnnotationPresent(Column.class)) {
            name = field.getAnnotationsByType(Column.class)[0].name();
        } else {
            name = field.getName();
        }
        return "" + name + "";
    }

    /**
     * *获取select 语句中使用的列，只要特点是前面可能会指定表名
     *
     * @param tableClass 所属的类
     * @param column     列
     * @param tableMap   别名
     * @return 返回真实的列名
     */
    public static String getTrueSqlColumn(Class<?> tableClass, String column, HashMap<String, String> tableMap) {
        String name;
        String trueColumn;
        try {
            trueColumn = ORMUtil.getColumn(tableClass.getDeclaredField(column));
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            trueColumn = column;
        }

        String simpleName = getTrueTableName(tableClass);
        if (tableMap.containsKey(simpleName)) {
            String alias = tableMap.get(simpleName);
            if (alias != null && !alias.equals(simpleName)) {
                name = "`" + tableMap.get(simpleName) + "`.`" + trueColumn + "`";
            } else {
                name = "`" + simpleName + "`.`" + trueColumn + "`";
            }
        } else {
            name = "`" + simpleName + "`.`" + trueColumn + "`";
        }
        return name;
    }
}
