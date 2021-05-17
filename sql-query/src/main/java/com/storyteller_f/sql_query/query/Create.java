package com.storyteller_f.sql_query.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.storyteller_f.sql_query.annotation.Comment;
import com.storyteller_f.sql_query.annotation.DefaultValue;
import com.storyteller_f.sql_query.annotation.NoQuery;
import com.storyteller_f.sql_query.annotation.RealName;
import com.storyteller_f.sql_query.annotation.constraint.ForeignKey;
import com.storyteller_f.sql_query.annotation.constraint.Nullable;
import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.type.Change;
import com.storyteller.util.ORMUtil;

public class Create extends Executor implements Query, Change {
    private final Class<?> tableClass;

    public Create(Obtain obtain, Class<?> tableClass) {
        super(obtain);
        this.tableClass = tableClass;
    }

    @Override
    public String parse(boolean safe) throws UnexpectedException {
        StringBuilder stringBuilder = new StringBuilder("create table " + ORMUtil.getTrueTableName(tableClass) + "(");
        Field[] fields = tableClass.getDeclaredFields();
        int primaryKeyCount=0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                primaryKeyCount++;
            }
        }
        HashMap<String, com.storyteller_f.sql_query.query.ForeignKey> foreignKeyMap=new HashMap<>();
        List<String> primaryKeyStrings=new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(NoQuery.class)) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) continue;
            String columnName = ORMUtil.getColumn(field);
            stringBuilder.append("\t").append(columnName);
            Annotation annotation=getColumnAnnotation(field);
            String columnType=null;
            if (annotation!=null) {//主要根据annotation 检测类型
                columnType =ORMUtil.getType(annotation);
            }
            if (columnType==null) {
                columnType = getDefaultName(field);
            }
            stringBuilder.append(" ").append(columnType);
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                if (primaryKeyCount==1) {
                    stringBuilder.append(" primary key");
                }else {
                    primaryKeyStrings.add(columnName);
                }
            }
            if (field.isAnnotationPresent(Nullable.class)) {
                stringBuilder.append(" not null ");
            }
            if (field.isAnnotationPresent(DefaultValue.class)) {
                stringBuilder.append(" default ");
                DefaultValue defaultValue=field.getDeclaredAnnotation(DefaultValue.class);
                stringBuilder.append(defaultToTrueString(defaultValue, field.getType()));
            }
            if (field.isAnnotationPresent(ForeignKey.class)) {
                ForeignKey foreignKey=field.getDeclaredAnnotation(ForeignKey.class);
                foreignKeyMap.put(columnName,new com.storyteller_f.sql_query.query.ForeignKey(foreignKey.tableClass(),foreignKey.column()));
            }
            if (field.isAnnotationPresent(Comment.class)) {
                Comment comment=field.getDeclaredAnnotation(Comment.class);
                stringBuilder.append(" comment ").append(comment.comment());
            }else {
                if (field.isAnnotationPresent(RealName.class)) {
                    RealName realName=field.getDeclaredAnnotation(RealName.class);
                    if (realName.alsoComment()) {
                        stringBuilder.append(" comment '").append(realName.name()).append("'");
                    }
                }
            }

            if (i != fields.length - 1) {
                stringBuilder.append(",\n");
            }
        }
        if (primaryKeyCount>1) {
            stringBuilder.append(",\n primary key(");
            for (String string : primaryKeyStrings) {
                stringBuilder.append(string).append(" ,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            stringBuilder.append(")");
        }
        Set<String> set=foreignKeyMap.keySet();
        for (String key : set) {
            com.storyteller_f.sql_query.query.ForeignKey foreignKey = foreignKeyMap.get(key);
            stringBuilder.append("\n\tforeign key ").append(key).append(" references ")
                    .append(ORMUtil.getTrueTableName(foreignKey.getTableClass()))
                    .append("(").append(foreignKey.getColumn())
                    .append("),");
        }
        if (stringBuilder.charAt(stringBuilder.length()-1)==',') {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        stringBuilder.append("\n);");
        if (tableClass.isAnnotationPresent(Comment.class)) {
            Comment comment=tableClass.getDeclaredAnnotation(Comment.class);
            stringBuilder.append(" comment ").append(comment.comment()).append("';\n");
        }
        return stringBuilder.toString();
    }

    private String getDefaultName(Field field) {
        switch (field.getType().toString()) {
            case "int": {
                return "int";
            }
            case "char":{
            	return "char(1)";
            }
            case "class java.lang.String":{
                return "varchar(20)";
            }
            default:
                throw new IllegalArgumentException("Unexpected type: " + field.getType().toString());
        }
    }
    /**
     * *将默认值转换成query中的一部分
     * @param defaultValue
     * @param valueClass
     * @return
     */
    private String defaultToTrueString(DefaultValue defaultValue,Class<?> valueClass) {
        if (valueClass.getSimpleName().equals("String")) {
            return "'"+defaultValue.value()+"'";
        }
        return defaultValue.value();
    }
    /**
     * *获取第一个末尾带有@Column 的注解，意思是设置多个此类型的注解是无效的
     * @param field
     * @return
     */
    private Annotation getColumnAnnotation(Field field) {
        Annotation[] annotations=field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            String simpleName = annotation.annotationType().getSimpleName();
			if (simpleName.endsWith("Column")&&!simpleName.equals("Column")) {
                return annotation;
            }
        }
        return null;
    }

    @Override
    public int execute() throws Exception {
        return obtain.getInt(null);
    }
}
