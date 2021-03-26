package com.storyteller_f.easyorm_search;

import com.storyteller_f.sql_query.query.query.ExpressionQuery;

public abstract class SearchPattern {
    public abstract ExpressionQuery[] parse();

    public boolean stringNotEmpty(String str) {
        if (str == null) {
            return false;
        }
        return str.trim().length() != 0;
    }
}
