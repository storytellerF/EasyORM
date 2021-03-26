package com.storyteller_f.sql_query.obtain;

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

    public DatabaseTable(Columns columns) {
        int columnCount=columns.getCount();
        init(columnCount);
        for (int i1 = 0; i1 < columnCount; i1++) {
            /*
             * 获取到列类型和列名
             */
            String columnLabel = columns.getName(i1);
            String columnTypeName = columns.getType(i1);
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
