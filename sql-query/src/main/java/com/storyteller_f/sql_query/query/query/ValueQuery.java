package com.storyteller_f.sql_query.query.query;

import com.storyteller_f.sql_query.query.Query;
import com.storyteller_f.sql_query.query.Value;
import com.storyteller_f.sql_query.query.expression.ValueExpression;
import com.storyteller_f.sql_query.util.ORMUtil;

public class ValueQuery extends Value implements Query {
    private ValueExpression<?> pointer;
    private ValueExpression<?> valueExpression;

    public ValueExpression<?> getValueExpression() {
        return valueExpression;
    }

    public ValueQuery() {
        super();
    }

    @Override
    public String parse(boolean safe) throws Exception {
        StringBuilder front = new StringBuilder("(");
        ValueExpression<?> valueExpression = this.valueExpression;
        StringBuilder back = new StringBuilder("values(");
        while (valueExpression != null) {
            String column = ORMUtil
                    .getColumn(valueExpression.getTableClass().getDeclaredField(valueExpression.getFieldName()));
            front.append(column);
            if (valueExpression.next != null) {
                front.append(",");
            }
            if (safe)
                back.append("?");
            else
                back.append(objectToString(valueExpression.getValue()));
            if (valueExpression.next != null) {
                back.append(",");
            }
            valueExpression = (ValueExpression<?>) valueExpression.next;
        }
        front.append(")");
        back.append(")");
        return "" + front + " " + back;
    }

    public void value(ValueExpression<?> valueExpression2) {
        if (pointer == null) {
            pointer = valueExpression2;
            this.valueExpression = pointer;
        } else {
            pointer.next = valueExpression2;
            pointer = valueExpression2;
        }
    }
}
