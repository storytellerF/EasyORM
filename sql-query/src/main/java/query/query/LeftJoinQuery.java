package query.query;

public class LeftJoinQuery extends JoinQuery{
    public LeftJoinQuery(Class<?> tableClass) {
        super(tableClass);
    }

    @Override
    public String parse(boolean safe) throws Exception {
        return "left"+ super.parse(safe);
    }
}
