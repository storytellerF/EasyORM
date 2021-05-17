package com.storyteller_f.sql_query.query.type;

import com.storyteller_f.sql_query.annotation.NoQuery;
import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.query.ExecutableQuery;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;
import com.storyteller.util.ORMUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Changer<OBJECT_TYPE> extends ExecutableQuery<Changer<OBJECT_TYPE>> implements Change {

	public Changer(Obtain obtain) {
		super(obtain);
	}
	public Changer<OBJECT_TYPE> setObject(OBJECT_TYPE object_type) throws Exception {
		table(object_type.getClass());
		Class<?> classOfParam= object_type.getClass();
		Field[] fields=classOfParam.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(NoQuery.class)) {
				continue;
			}
			if (Modifier.isStatic(field.getModifiers())) continue;
			String fieldName=field.getName();
			Object value =ORMUtil.getValue(object_type, classOfParam, field);
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
