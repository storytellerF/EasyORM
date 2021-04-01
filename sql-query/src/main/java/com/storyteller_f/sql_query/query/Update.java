package com.storyteller_f.sql_query.query;

import java.lang.reflect.Field;

import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.expression.EqualExpression;
import com.storyteller_f.sql_query.query.expression.SetExpression;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;
import com.storyteller_f.sql_query.query.query.SetQuery;
import com.storyteller_f.sql_query.query.query.WhereQuery;
import com.storyteller_f.sql_query.query.type.Changer;

/**
 * 除非使用update 方法，必须指定表名
 * @param <OBJECT_TYPE>
 */
@SuppressWarnings("unused")
public class Update<OBJECT_TYPE> extends Changer<OBJECT_TYPE> {
	private final SetQuery setQuery;
	private final WhereQuery whereQuery;

	public Update(Obtain obtain) {
		super(obtain);
		setQuery = new SetQuery();
		whereQuery = new WhereQuery();
	}

	public Update<OBJECT_TYPE> set(SetExpression<?> setExpression) {
		setQuery.set(setExpression);
		return this;
	}
	public Update<OBJECT_TYPE> update(OBJECT_TYPE t) throws Exception {
		setObject(t);
		return this;
	}

	@Override
	public boolean pass(Field field, String columnName, Object value) {
		if (field.isAnnotationPresent(PrimaryKey.class)) {
			and(new EqualExpression<>(field.getDeclaringClass(), columnName, value));
			return true;
		}
		return super.pass(field, columnName, value);
	}

	@Override
	public void add(Class<?> lax, String fieldName, Object value) {
		set(new SetExpression<>(lax, fieldName, value));
	}

	@Override
	public String parse(boolean safe) throws Exception {
		super.parse(safe);
		if (whereQuery.getHeaderPointer()==null) System.out.println("警告，未设定条件");
		return String.format("update %s %s %s;", tableQuery.parse(safe), setQuery.parse(safe), whereQuery.parse(safe));
	}

	@Override
	public ExpressionQuery getExpressionQuery() {
		ExpressionQuery expressionQuery;
		ExpressionQuery header;
		SetExpression<?> setExpression = setQuery.getSetExpression();
		try {
			expressionQuery = (ExpressionQuery) setExpression.clone();
			header=expressionQuery;
			setExpression=(SetExpression<?>) setExpression.next;
			while (setExpression != null) {
				ExpressionQuery clone = (ExpressionQuery) setExpression.clone();
//				System.out.println(clone.getClass());
				expressionQuery.next(clone);
				expressionQuery=expressionQuery.next;
				setExpression = (SetExpression<?>) setExpression.next;
			}
			ExpressionQuery expressionQuery2 = whereQuery.getHeaderPointer();
			while (expressionQuery2 != null) {

				Object clone = expressionQuery2.clone();
				ExpressionQuery clone2 = (ExpressionQuery) clone;
				expressionQuery.next(clone2);
				expressionQuery=expressionQuery.next;
				expressionQuery2 = expressionQuery2.next;
			}
			return header;
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public Update<OBJECT_TYPE> and(ExpressionQuery expressionQuery) {
		whereQuery.and(expressionQuery, tableQuery.getTableMap());
		return this;
	}
}
