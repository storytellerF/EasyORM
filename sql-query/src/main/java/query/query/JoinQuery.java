package query.query;

import java.util.HashMap;

public class JoinQuery extends AbstractExpressionList {
    private final Class<?> tableClass;
    public JoinQuery(Class<?> tableClass) {
        this.tableClass=tableClass;
    }

    public void on(ExpressionQuery expressionQuery, HashMap<String, String> tableMap) {
        next(expressionQuery, tableMap);
    }
    public void and(ExpressionQuery expressionQuery, HashMap<String, String> tableMap) {
        next(expressionQuery, tableMap);
    }
    @Override
    public String parse(boolean safe) throws Exception {
        return " join `"+tableClass.getSimpleName()+"` on "+super.parse(safe);
    }

}
