package com.storyteller_f.sql_query.obtain;

/**
 * 目前来看是个多余的结构，后期考虑移除
 */
public class DatabaseTable {
    String[] columnNames;
    /*
     * *存储数据库所有的列类型
     */
    String[] columnType;

    public DatabaseTable(int columnCount) {
        init(columnCount);
    }

    private void init(int columnCount) {
        columnNames = new String[columnCount];
        columnType = new String[columnCount];
    }

    public DatabaseTable(DatabaseColumnsInfo databaseColumnsInfo) {
        int columnCount= databaseColumnsInfo.getCount();
        init(columnCount);
        for (int i1 = 0; i1 < columnCount; i1++) {
            /*
             * 获取到列类型和列名
             */
            String columnLabel = databaseColumnsInfo.getName(i1);
            String columnTypeName = databaseColumnsInfo.getType(i1);
            add(i1, columnLabel, columnTypeName);
        }
    }

    /**
     * 数据库中真实的列名，未进行任何处理
     * @param index
     * @return
     */
    public String getName(int index) {
        return columnNames[index];
    }

    public String getType(int index) {
        return columnType[index];
    }
    public void add(int index,String name, String type) {
        columnNames[index]=name;
        columnType[index]=type;
    }

}
