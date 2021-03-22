package com.storyteller_f.sql_query.query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Value {
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
    public String objectToString(Object value) {
        if (value==null) return "";
        if (value instanceof String) {
            return "'" + value + "'";
        } else if (value instanceof Integer) {
            return String.valueOf(value);
        }else if (value instanceof Date){
            return simpleDateFormat.format(value);
        }
        throw new IllegalArgumentException("不支持的类型:" + value.getClass());
    }
}
