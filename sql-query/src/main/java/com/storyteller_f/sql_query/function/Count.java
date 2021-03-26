package com.storyteller_f.sql_query.function;

import com.storyteller_f.sql_query.annotation.Column;

public class Count extends Function {
    @Column(name = "count(*)")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
