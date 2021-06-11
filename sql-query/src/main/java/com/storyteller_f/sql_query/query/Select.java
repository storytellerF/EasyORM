package com.storyteller_f.sql_query.query;

import com.storyteller_f.sql_query.annotation.Children;
import com.storyteller_f.sql_query.annotation.Convert;
import com.storyteller_f.sql_query.obtain.FieldWithPath;
import com.storyteller_f.sql_query.obtain.MethodWithPath;
import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.expression.TwoExpression;
import com.storyteller_f.sql_query.query.query.*;
import com.storyteller_f.sql_query.query.result.Result;
import com.storyteller_f.sql_query.query.type.Search;
import com.storyteller_f.sql_query.util.EasyCache;
import com.storyteller_f.sql_query.util.ReflectUtil;
import org.apache.commons.text.CaseUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Select<RETURN_TYPE> extends ExecutableQuery<Select<RETURN_TYPE>> implements Search {
    private final SelectQuery selectQuery;
    private final LimitQuery limitQuery;
    private final WhereQuery whereQuery;
    private final GroupQuery groupQuery;
    private JoinQuery joinQuery;
    private Class<RETURN_TYPE> returnType;

    public Select(Obtain obtain) {
        super(obtain);
        selectQuery = new SelectQuery();
        selectQuery.setTableMap(tableQuery.getTableMap());
        limitQuery = new LimitQuery();
        whereQuery = new WhereQuery();
        groupQuery = new GroupQuery();
    }

    public Class<RETURN_TYPE> getReturnType() {
        return returnType;
    }

    protected void setReturnType(Class<RETURN_TYPE> returnType) {
        this.returnType = returnType;
    }

    /**
     * @param columnClass 需要查询的表
     * @return 返回当前对象
     */
    public Select<RETURN_TYPE> select(Class<RETURN_TYPE> columnClass) {
        selectQuery.select(columnClass);
        setReturnType(columnClass);
        return this;
    }

    public Select<RETURN_TYPE> function(Class<RETURN_TYPE> columnClass, Class<?> tableClass) {
        select(columnClass);
        return this;
    }

    /**
     * @param tableClass 查询的表，查询到的数据需要生成对象
     * @param trueTable  数据库中真实存在的表，被查的表
     * @return 返回当前对象
     */
    public Select<RETURN_TYPE> select(Class<RETURN_TYPE> tableClass, Class<?> trueTable) {
        selectQuery.select(tableClass, trueTable);
        setReturnType(tableClass);
        return this;
    }

    @Override
    public String parse(boolean safe) throws Exception {
        super.parse(safe);
        if (tableQuery.count() == 0) {
            table(getReturnType());
        }
        return String.format("%s %s %s %s %s %s", selectQuery.parse(safe), tableQuery.parse(safe), whereQuery.parse(safe),
                joinQuery != null ? joinQuery.parse(safe) : "", groupQuery.parse(safe), limitQuery.parse(safe)).trim() + ";";
    }

    public Select<RETURN_TYPE> limit(int offset, int count) {
        limitQuery.limit(offset, count);
        return this;
    }

    public Select<RETURN_TYPE> limit(int count) {
        return limit(0, count);
    }

    public Select<RETURN_TYPE> and(ExpressionQuery[] expressionQueries) {
        for (ExpressionQuery expressionQuery : expressionQueries) {
            and(expressionQuery);
        }
        return this;
    }

    public Select<RETURN_TYPE> and(ExpressionQuery expressionQuery) {
        if (expressionQuery instanceof TwoExpression) {
            TwoExpression<?> expressionQuery1 = (TwoExpression<?>) expressionQuery;
            if (expressionQuery1.getTableClass() == null) {
                expressionQuery1.setTableClass(getReturnType());
            }
        }
        whereQuery.and(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<RETURN_TYPE> where(ExpressionQuery... executableQueries) {
        return and(executableQueries);
    }

    public Select<RETURN_TYPE> leftJoin(Class<?> tableClass, ExpressionQuery expressionQuery) {
        joinQuery = new LeftJoinQuery(tableClass);
        joinQuery.on(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<RETURN_TYPE> joinOn(ExpressionQuery expressionQuery) {
        joinQuery.and(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<RETURN_TYPE> rightJoin(Class<?> tableClass, ExpressionQuery expressionQuery) {
        joinQuery = new RightJoinQuery(tableClass);
        joinQuery.on(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<RETURN_TYPE> groupBy(Class<?> tableClass, String column, ExpressionQuery expressionQuery) {
        groupQuery.groudBy(tableClass, expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<RETURN_TYPE> having(ExpressionQuery expressionQuery) {
        groupQuery.havingAnd(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    @Override
    public ExpressionQuery getExpressionQuery() {
        return whereQuery.getHeaderPointer();
    }

    public WhereQuery getWhereQuery() {
        return whereQuery;
    }

    public List<RETURN_TYPE> execute() throws Exception {
        String parse = parse(true);
        List<?> request = EasyCache.getEasyCache().request(parse);
        if (request != null) {
            return (List<RETURN_TYPE>) request;
        }
        EasyCache.getEasyCache().tellWork(parse);
        Result result = obtain.getResult(this);
        List<RETURN_TYPE> list = new ArrayList<>();
        while (result.hasNext()) {
            Object[] data = result.next();
            boolean isString = getReturnType().equals(String.class);
            boolean isInteger = getReturnType().equals(Integer.class);
            if (isString || isInteger) {// 返回类型是基本类型
                Object datum = data[0];
                if (getReturnType().isInstance(datum)) {
//                    throw new Exception("需要修复");
                    list.add((RETURN_TYPE) datum);// 默认只有第一个参数有效
                } else {
                    throw new Exception("获得的数据，不符合要求");
                }
                continue;
            }
            Constructor<RETURN_TYPE> constructor = getReturnType().getConstructor();

            RETURN_TYPE instance = constructor.newInstance();// 利用反射实例化到对象
            init(instance);
            for (int i = 0; i < result.getColumnCount(); i++) {// 进行赋值
                MethodWithPath methodWithPath = result.getMethod(i);
                Object datum = data[i];
                if (methodWithPath != null) {// 字段的setter 方法
                    Class<?> object = getObject(methodWithPath.path, getReturnType());
                    methodWithPath.method.invoke(instance, datum);
                } else {
                    FieldWithPath fieldWithPath = result.getField(i);
                    Field field = fieldWithPath.field;
                    String typeName = field.getType().getName();
                    System.out.println("type_name:" + typeName);

                    // 获得方法
                    String convertMethodName;
                    if (field.isAnnotationPresent(Convert.class)) {//指明了转换函数
                        Convert convertAnnotation = field.getAnnotation(Convert.class);
                        convertMethodName = convertAnnotation.name();
                    } else {
                        if (result.typeCoincide(i)) {//无需使用转换函数
                            fieldSetValue(instance, datum, field);
                            continue;
                        } else {//使用默认转换函数名
                            convertMethodName = reverse(field.getName());
                        }
                    }
                    //需要使用转换函数
                    Method convert = getReturnType().getDeclaredMethod(convertMethodName, result.getType(i));
                    fieldSetValue(instance, convert.invoke(instance, datum), field);
                }
            }
            list.add(instance);
        }
        EasyCache.getEasyCache().workDone(parse, list);
        return list;

    }

    private void fieldSetValue(RETURN_TYPE instance, Object datum, Field field) throws IllegalAccessException {
        boolean a = field.isAccessible();
        field.setAccessible(true);
        field.set(instance, datum);
        field.setAccessible(a);
    }


    /**
     * 根据路径获取指定的对象，应该保证对象已经实例化，因为此方法，不会进行实例化操作
     *
     * @param path 父子关系路径
     * @param cls 当前查找的类
     * @return 返回查找到的类
     */
    private Class<?> getObject(String path, Class<?> cls) throws NoSuchFieldException {
        if (path.length() == 0) {
            return cls;
        }
        int i = path.indexOf("/");
        String name = path.substring(0, i);
        return getObject(path.substring(i + 1), cls.getDeclaredField(name).getType());
    }

    private void init(Object instance) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Field> allField = ReflectUtil.getAllField(instance.getClass());
        for (Field field : allField) {
            //todo 对带有children 的字段进行类型检查
            if (field.isAnnotationPresent(Children.class)) {
                //初始化值
                Constructor<?> constructor = field.getType().getConstructor();
                Object o = constructor.newInstance();
                fieldSetValue(instance, field, o);
                init(o);
            }
        }
    }

    private void fieldSetValue(Object instance, Field field, Object value) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.set(instance, value);
        field.setAccessible(accessible);
    }

    public RETURN_TYPE one() throws Exception {
        List<RETURN_TYPE> result = execute();
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    private Object checkInstance(Field field, RETURN_TYPE instance) throws Exception {
        Method getMethod = getGetMethod(field.getName());
        if (getMethod != null) {
            Object invoke = getMethod.invoke(instance);
            if (invoke != null) {
                return invoke;
            }
        }
        boolean d = field.isAccessible();
        field.setAccessible(true);
        Object o = field.get(instance);
        if (o != null) {
            return o;
        }
        field.setAccessible(d);

        //开始初始化
        Constructor<?> constructor = field.getType().getConstructor();
        Object ob = constructor.newInstance();
        Method method1 = getSetMethod(field.getName(), field.getClass());
        if (method1 != null) {
            method1.invoke(instance, ob);
        } else {
            Field childField = getChildField(field.getType());
            if (childField == null) {
                throw new Exception();
            }
            fieldSetValue(instance, ob, childField);
        }
        return ob;

    }

    private Method getGetMethod(String name) {
        try {
            return getReturnType().getDeclaredMethod("get" + CaseUtils.toCamelCase(name, true));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Method getSetMethod(String name, Class<?> c) {
        try {
            return getReturnType().getDeclaredMethod("set" + CaseUtils.toCamelCase(name, true), c);
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    private Field getChildField(Class<?> declaringClass) {
        Field[] declaredFields = getReturnType().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getType().equals(declaringClass)) {
                return declaredField;
            }
        }
        return null;
    }

    public String reverse(String origin) {
        char[] chars = new char[origin.length()];
        for (int i = 0; i < origin.length(); i++) {
            chars[i] = origin.charAt(origin.length() - i - 1);
        }
        return new String(chars);
    }
}
