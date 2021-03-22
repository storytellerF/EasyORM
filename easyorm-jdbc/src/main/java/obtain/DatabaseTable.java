package obtain;

import java.util.Arrays;

public class DatabaseTable {
    String[] columnNames;
    /*
     * *存储数据库所有的列类型
     */
    String[] columnType;
    /*
     * *记录列是否含有注解，这个个数不是有模型类决定，而是由数据库端决定， *意味着模型类中可以存储其他用不到的字段而不会出现 *任何错误
     */
    int[] hasAnnotations;

    public DatabaseTable(int columnCount) {
        columnNames = new String[columnCount];
        columnType = new String[columnCount];
        hasAnnotations = new int[columnCount];
        Arrays.fill(hasAnnotations,-1);
    }

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

    public void add(int index, int annotation) {
        hasAnnotations[index]=annotation;
    }

    public int getAnnotation(int index) {
        return hasAnnotations[index];
    }
}
