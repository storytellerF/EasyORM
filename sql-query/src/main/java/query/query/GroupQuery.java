package query.query;

import java.util.HashMap;

import util.ORMUtil;

public class GroupQuery extends AbstractExpressionList {
    private Class<?> tableClass;
    public void groudBy(Class<?> tableClass, ExpressionQuery expressionQuery, HashMap<String, String> tableMap) {
        this.tableClass=tableClass;
        next(expressionQuery, tableMap);
    }
    public void havingAnd(ExpressionQuery expressionQuery, HashMap<String, String> tableMap) {
        next(expressionQuery, tableMap);
    }
    @Override
    public String parse(boolean safe) throws Exception {
        if (tableClass!=null) {
            return "group by "+ORMUtil.getTrueTableName(tableClass)+expression.parse(safe);
        }
        return "";
    }
}
