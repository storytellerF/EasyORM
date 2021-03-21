package query.expression.alias;

import query.expression.SmallThanExpression;

public class ST<T> extends SmallThanExpression<T> {
    public ST(String fieldName, T value) {
        super(fieldName, value);
    }

    public ST(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }
}
