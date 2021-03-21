package query.type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import annotation.NoQuery;
import obtain.Obtain;
import query.Insert;
import query.query.ExecutableQuery;
import query.query.ExpressionQuery;
import util.ORMUtil;

public abstract class Changer<T> extends ExecutableQuery<Insert<T>> implements Change {

	public Changer(Obtain obtain) {
		super(obtain);
	}
	public Changer<T> setObject(T t) throws Exception {
		table(t.getClass());
		Class<?> classOfParam=t.getClass();
		Field[] fields=classOfParam.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(NoQuery.class)) {
				continue;
			}
			if (Modifier.isStatic(field.getModifiers())) continue;
			String fieldName=field.getName();
			Object value =ORMUtil.getValue(t, classOfParam, field);

			if (pass(field,fieldName,value)) {
				continue;
			}
			add(classOfParam, fieldName, value);
		}

		return this;
	}

	/**
	 * 交由子类处理
	 * @param field 字段
	 * @param columnName 名称
	 * @param value 值
	 * @return 如果返回true，父类不再处理
	 */
	public boolean pass(Field field, String columnName, Object value) {
		return false;
	}


	public abstract void add(Class<?> claxx, String fieldName, Object value);
	@Override
	public int execute() throws Exception {
		return obtain.getInt(this);
	}

	@Override
	public abstract ExpressionQuery getExpressionQuery();
}
