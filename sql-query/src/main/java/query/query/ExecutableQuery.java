package query.query;

import query.Executor;
import query.Query;
import obtain.Obtain;
import util.ORMUtil;

public abstract class ExecutableQuery<T extends ExecutableQuery<T>> extends Executor implements Query{
    private Class<?> returnType;
    protected TableQuery tableQuery;
    public ExecutableQuery(Obtain obtain) {
        super(obtain);
        tableQuery=new TableQuery();
    }
    /**
     * *继承此类的的类重载此方法时最好调用super.parse();
     */
    @Override
    public String parse(boolean safe) throws Exception {
    	if (tableQuery.count()==0) {
			table(getReturnType());
		}
        return null;
    }

    protected void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
    public Class<?> getReturnType() {
        return returnType;
    }
    public abstract ExpressionQuery getExpressionQuery();
    @SuppressWarnings("unchecked")
	public T table(String name) {
        tableQuery.table(name);
        return (T) this;
    }
    public T table(Class<?> tableClass) {
        String name=ORMUtil.getTrueTableName(tableClass);
        return table(name);
    }
    @SuppressWarnings("unchecked")
	public T table(String name,String alias) {
        tableQuery.table(name,alias);
        return (T) this;
    }
    public T table(Class<?> tableClass,String alias) {
        String name;
        name =ORMUtil. getTrueTableName(tableClass);
        return table(name,alias);
    }

}
