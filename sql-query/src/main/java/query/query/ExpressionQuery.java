package query.query;

import query.Query;
import query.Value;

import java.util.HashMap;

public abstract class ExpressionQuery extends Value implements Query{
    protected HashMap<String, String> tableMap;
    public ExpressionQuery next;

//    public void and(ExpressionQuery expressionQuery) {
//        next=expressionQuery;
//    }
    public abstract String parse(boolean safe) throws Exception;
    @Override
	public Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }
    public ExpressionQuery next() {
        return next;
    }
    public ExpressionQuery next(ExpressionQuery expressionQuery) {
        next=expressionQuery;
        return this;
    }
    public void setTableMap(HashMap<String, String> tableMap) {
        this.tableMap = tableMap;
    }
}
