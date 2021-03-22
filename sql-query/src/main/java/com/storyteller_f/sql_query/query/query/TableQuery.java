package com.storyteller_f.sql_query.query.query;
import java.util.HashMap;
import java.util.Set;

import com.storyteller_f.sql_query.query.Query;

public class TableQuery implements Query{
    private final HashMap<String, String> tableMap= new HashMap<>();

    public HashMap<String, String> getTableMap() {
        return tableMap;
    }
    public void table(String tableName) {
        tableMap.put(tableName, tableName);
    }
    public void table(String tableName,String alias) {
        tableMap.put(tableName, alias);
    }

    /**
     * 记录的个数
     * @return 个数
     */
    public int count() {
		return tableMap.size();
	}
    @Override
    public String parse(boolean safe) {
        if (tableMap.isEmpty()) {
            throw new NullPointerException("未指定表");
        }
        StringBuilder stringBuilder=new StringBuilder();
        Set<String> set=tableMap.keySet();
        for (String tableName : set) {
            String aliaString=tableMap.get(tableName);
            stringBuilder.append("`").append(tableName).append("`");
            if (aliaString!=null&&!aliaString.equals(tableName)) {
                stringBuilder.append(" as ").append(aliaString);
            }
            stringBuilder.append(",");
        }
        if (stringBuilder.charAt(stringBuilder.length()-1)==',') {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }

}
