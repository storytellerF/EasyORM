package query.expression;

public class EqualExpression<T> extends ArithMeticalExpression<T> {
    public EqualExpression(Class<?> tableClass,String column, T value ) {
        super(tableClass,column, value);
    }

    public EqualExpression(String fieldName, T value) {
        super(fieldName, value);
    }

    @Override
    protected String parse(String column, String right,boolean safe) {
        if (!safe) {
            return column+" = "+right;
        }
        return column+" = ?";
    }
}
