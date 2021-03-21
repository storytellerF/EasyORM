package query;

import annotation.Convert;
import obtain.Obtain;
import org.apache.commons.text.CaseUtils;
import query.expression.TwoExpression;
import query.query.*;
import query.result.Result;
import query.type.Search;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Select<E> extends ExecutableQuery<Select<E>> implements Search {
    private final SelectQuery selectQuery;
    private final LimitQuery limitQuery;
    private final WhereQuery whereQuery;
    private JoinQuery joinQuery;
    private final GroupQuery groupQuery;

    public Select(Obtain obtain) {
        super(obtain);
        selectQuery = new SelectQuery();
        selectQuery.setTableMap(tableQuery.getTableMap());
        limitQuery = new LimitQuery();
        whereQuery = new WhereQuery();
        groupQuery = new GroupQuery();
    }

    /**
     * @param tableClass 需要查询的表
     * @return 返回当前对象
     */
    public Select<E> select(Class<?> tableClass) {
        selectQuery.select(tableClass);
        setReturnType(tableClass);
        return this;
    }

    /**
     * @param tableClass 查询的表，查询到的数据需要生成对象
     * @param trueTable  数据库中真实存在的表，被查的表
     * @return 返回当前对象
     */
    public Select<E> select(Class<?> tableClass, Class<?> trueTable) {
        selectQuery.select(tableClass, trueTable);
        setReturnType(tableClass);
        return this;
    }

    @Override
    public String parse(boolean safe) throws Exception {
        super.parse(safe);
        return String.format("%s %s %s %s %s %s;", selectQuery.parse(safe), tableQuery.parse(safe), whereQuery.parse(safe),
                joinQuery != null ? joinQuery.parse(safe) : "", groupQuery.parse(safe), limitQuery.parse(safe)).trim();
    }

    public Select<E> limit(int offset, int count) {
        limitQuery.limit(offset, count);
        return this;
    }

    public Select<E> limit(int count) {
        return limit(0, count);
    }

    public Select<E> and(ExpressionQuery expressionQuery) {
        if (expressionQuery instanceof TwoExpression){
            TwoExpression<?> expressionQuery1 = (TwoExpression<?>) expressionQuery;
            if (expressionQuery1.getTableClass()==null) {
                expressionQuery1.setTableClass(getReturnType());
            }
        }
        whereQuery.and(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<E> leftJoin(Class<?> tableClass, ExpressionQuery expressionQuery) {
        joinQuery = new LeftJoinQuery(tableClass);
        joinQuery.on(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<E> joinOn(ExpressionQuery expressionQuery) {
        joinQuery.and(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<E> rightJoin(Class<?> tableClass, ExpressionQuery expressionQuery) {
        joinQuery = new RightJoinQuery(tableClass);
        joinQuery.on(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<E> groupBy(Class<?> tableClass, String column, ExpressionQuery expressionQuery) {
        groupQuery.groudBy(tableClass, expressionQuery, tableQuery.getTableMap());
        return this;
    }

    public Select<E> having(ExpressionQuery expressionQuery) {
        groupQuery.havingAnd(expressionQuery, tableQuery.getTableMap());
        return this;
    }

    @Override
    public ExpressionQuery getExpressionQuery() {
        return whereQuery.getExpression();
    }

    public WhereQuery getWhereQuery() {
        return whereQuery;
    }

    @SuppressWarnings("unchecked")
    public List<E> execute() throws Exception {
        Result result = obtain.getResult(this);
        List<E> list = new ArrayList<>();
        while (result.hasNext()) {
            Object[] data = result.next();
            if (getReturnType().equals(String.class) || getReturnType().equals(Integer.class)) {// 返回类型是基本类型
                list.add((E) data[0]);// 默认只有第一个参数有效
                continue;
            }
            Constructor<?> constructor = getReturnType().getConstructor();
            E instance = (E) constructor.newInstance();// 利用放射实例化到对象
            for (int i = 0; i < result.getColumnCount(); i++) {// 进行赋值
                Method method = result.getMethod(i);
                Object datum = data[i];
                if (method != null) {// 字段的setter 方法
                    method.invoke(instance, datum);
                } else {
                    Field field = result.getField(i);
                    if (!field.getDeclaringClass().equals(getReturnType())) {
                        //检查响应对象是否实例化
                        Object o = checkInstance(getChildField(field.getDeclaringClass()), instance);
                        boolean c = field.isAccessible();
                        field.setAccessible(true);
                        if (datum.getClass().equals(Date.class)) {
                            field.set(o, new java.util.Date(((Date) datum).getTime()));
                        } else {
                            field.set(o, datum);
                        }
                        field.setAccessible(c);
                        continue;
                    }
                    String typeName = field.getType().getName();
                    System.out.println("field.name:" + typeName);
                    boolean a = field.isAccessible();
                    field.setAccessible(true);

                    // 获得方法
                    String convertMethodName;
                    if (field.isAnnotationPresent(Convert.class)) {
                        Convert convertAnnotation = field.getAnnotation(Convert.class);
                        convertMethodName = convertAnnotation.name();
                    } else {
                        if (result.typeCoincide(i)) {
                            field.set(instance, datum);
                            field.setAccessible(a);
                            continue;
                        } else {
                            convertMethodName = field.getName();
                        }
                    }
                    //需要使用转换函数
                    Method convert = getReturnType().getDeclaredMethod(convertMethodName, result.getType(i));
                    field.set(instance, convert.invoke(instance, datum));
                    field.setAccessible(a);
                }
            }
            list.add(instance);

        }
        return list;

    }

    public E one() throws Exception {
        List<E> execute = execute();
        if (execute.size() > 0) {
            return execute.get(0);
        } else {
            return null;
        }
    }

    private Object checkInstance(Field field, E instance) throws Exception {
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
            boolean b = childField.isAccessible();
            childField.setAccessible(true);
            childField.set(instance, ob);
            childField.setAccessible(b);
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
