package query.query;

import query.Query;
import query.expression.SetExpression;

public class SetQuery implements Query {
	public SetExpression<?> getPointer() {
		return pointer;
	}

	private SetExpression<?> setExpression;

	public SetExpression<?> getSetExpression() {
		return setExpression;
	}

	private SetExpression<?> pointer;

	@Override
	public String parse(boolean safe) throws Exception {
		String parse = setExpression.parse(safe);
		return "set " + parse;
	}

	public void set(SetExpression<?> setExpression) {
		if (pointer == null) {
			this.setExpression = setExpression;
			pointer = setExpression;
		} else {
			pointer.next = setExpression;
			pointer = setExpression;
		}

	}

}
