package query.query;

import query.Query;

public class LimitQuery implements Query{
    private int offset=-1;
    private int count=-1;
    @Override
    public String parse(boolean safe) {
        if (offset==-1&&count==-1) {
            return "";
        }
        return String.format("limit %d,%d", offset,count);
    }

    public void limit(int offset, int count) {
        this.count=count;
        this.offset=offset;
    }

}
