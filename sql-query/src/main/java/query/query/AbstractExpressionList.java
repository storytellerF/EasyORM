package query.query;

import java.util.HashMap;

import query.Query;

public abstract class AbstractExpressionList implements Query {
	protected ExpressionQuery headerPointer;
	protected ExpressionQuery pointer;

	public ExpressionQuery getHeaderPointer() {
		return headerPointer;
	}

	public void next(ExpressionQuery expressionQuery, HashMap<String, String> tableMap) {
		if (pointer == null) {
			this.headerPointer = expressionQuery;
			expressionQuery.setTableMap(tableMap);
			pointer = this.headerPointer;
		} else {
			pointer.next = expressionQuery;
			expressionQuery.setTableMap(tableMap);
			pointer = expressionQuery;
		}

	}

	public String parse(boolean safe) throws Exception {
		if (headerPointer != null) {
			return headerPointer.parse(safe);
		}
		return "";
	}
}
