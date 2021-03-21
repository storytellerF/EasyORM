package query;

import java.lang.reflect.Field;

import annotation.constraint.PrimaryKey;
import obtain.Obtain;
import query.expression.EqualExpression;
import query.expression.SetExpression;
import query.query.ExpressionQuery;
import query.query.SetQuery;
import query.query.WhereQuery;
import query.type.Changer;

public class Update<T> extends Changer<T> {
	private final SetQuery setQuery;
	private final WhereQuery whereQuery;

	public Update(Obtain obtain) {
		super(obtain);
		setQuery = new SetQuery();
		whereQuery = new WhereQuery();
		setReturnType(Integer.class);
	}

	public Update<T> set(SetExpression<?> setExpression) {
		setQuery.set(setExpression);
		return this;
	}
	public Update<T> update(T t) throws Exception {
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
	public void add(Class<?> claxx, String fieldName, Object value) {
		set(new SetExpression<>(claxx, fieldName, value));
	}

	@Override
	public String parse(boolean safe) throws Exception {
		super.parse(safe);
		if (whereQuery.getExpression()==null) System.out.println("警告，未设定条件");
		return "update " + tableQuery.parse(safe) + " " + setQuery.parse(safe) + " " + whereQuery.parse(safe) + ";";
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
			ExpressionQuery expressionQuery2 = whereQuery.getExpression();
			while (expressionQuery2 != null) {

				Object clone = expressionQuery2.clone();
				ExpressionQuery clone2 = (ExpressionQuery) clone;
//				System.out.println(expressionQuery2.getClass().getName() + " to:" + clone.getClass().getName() + " 2:"
//						+ clone2.getClass().getName());
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

	public Update<T> and(ExpressionQuery expressionQuery) {
		whereQuery.and(expressionQuery, tableQuery.getTableMap());
		return this;
	}
}
