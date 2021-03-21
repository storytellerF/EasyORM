package query.expression.alias;

import query.expression.GreaterThanExpression;

public class GT<T> extends GreaterThanExpression<T> {
    public GT(Class<?> tableClass, String fieldName, T value) {
        super(tableClass, fieldName, value);
    }

    public GT(String fieldName, T value) {
        super(fieldName, value);
    }
}
