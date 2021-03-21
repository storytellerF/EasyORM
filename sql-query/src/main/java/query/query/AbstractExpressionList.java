package query.query;

import java.util.HashMap;

import query.Query;
import query.query.ExpressionQuery;

public abstract class AbstractExpressionList implements Query {
	protected ExpressionQuery expression;
	protected ExpressionQuery pointer;

	public ExpressionQuery getExpression() {
		return expression;
	}

	public void next(ExpressionQuery expressionQuery, HashMap<String, String> tableMap) {
		if (pointer == null) {
			this.expression = expressionQuery;
			expressionQuery.setTableMap(tableMap);
			pointer = this.expression;
		} else {
			pointer.next = expressionQuery;
			expressionQuery.setTableMap(tableMap);
			pointer = expressionQuery;
		}

	}

	public String parse(boolean safe) throws Exception {
		if (expression != null) {
			return expression.parse(safe);
		}
		return "";
	}
}
