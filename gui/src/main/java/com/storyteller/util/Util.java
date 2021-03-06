package com.storyteller.util;

import com.storyteller.gui.model.Constraint;
import com.storyteller.gui.model.InformationSchemaColumn;
import com.storyteller.gui.model.Table;
import com.storyteller_f.easyorm_jdbc.JDBCObtain;
import com.storyteller_f.relay_message.RelayMessage;
import com.storyteller_f.sql_query.query.Select;
import com.storyteller_f.sql_query.query.expression.alias.EE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {

    /**
     * 获取第一个结尾是Column的注解，根据设定，结尾是Column的注解是数据库中的字段类型
     *
     * @param field 相应的字段
     * @return 返回注解
     */
    public static Annotation getColumnAnnotation(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().endsWith("Column")) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 测试一个数据库连接
     *
     * @param url      地址
     * @param user     登录的用户名
     * @param password 登录的密码
     * @return
     */
    public static RelayMessage testConnection(String url, String user, String password) {
        try {
            DriverManager.getConnection(url, user, password).close();
            return new RelayMessage(true, "");
        } catch (SQLException e) {
//            e.printStackTrace();
            return new RelayMessage(false, e.getMessage());
        }
    }

    /**
     * 从指定内容中获取一个数字
     *
     * @param content
     * @return 返回获取到的数字
     */
    public static int getNumber(String content) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c <= '9' && c >= '0') {
                stringBuilder.append(c);
            }
        }
        return Integer.parseInt(stringBuilder.toString());
    }

    /**
     * 根据顺序获取两个数字
     *
     * @param content
     * @return 返回一个数组，索引为0 的是第一个数字，索引为1 的是第二个数字
     */
    public static int[] getTwo(String content) {
        StringBuilder[] stringBuilder = new StringBuilder[2];
        for (int i = 0; i < 2; i++) {
            stringBuilder[i] = new StringBuilder();
        }
        int index = 0;
        for (int i = 0; i < content.length(); i++) {
            if (index == 2) {
                break;
            }
            char c = content.charAt(i);
            if (c <= '9' && c >= '0') {
                stringBuilder[index].append(c);
            } else {
                if (stringBuilder[index].length() > 0) {
                    index++;
                }
            }
        }
        int[] ii = new int[2];
        for (int i = 0; i < 2; i++) {
            ii[i] = Integer.parseInt(stringBuilder[i].toString());
        }
        return ii;
    }

    /**
     * @param tableName  查询的表名
     * @param database   查询的数据库名
     * @param connection 用来进行查询的connection 对象
     * @return table 返回table对象
     * @throws Exception sql-com.storyteller_f.sql_query.query 执行异常
     */
    public static Table geTableDetail(String tableName, String database, Connection connection) throws Exception {
        System.out.println("name:" + tableName + ";database:" + database);
        Table table;
        table = new Table(tableName);
        JDBCObtain JDBCObtain = new JDBCObtain(connection);
        Select<InformationSchemaColumn> select = new Select<>(JDBCObtain);
        select.select(InformationSchemaColumn.class).table(com.storyteller_f.easyorm_jdbc.JDBCObtain.MYSQL_INFORMATION_SCHEMA + "`.Columns`")
                .and(new EE<>(InformationSchemaColumn.class, "table_name", tableName).next(new EE<>(InformationSchemaColumn.class, "table_schema", database)));
        System.out.println(select.parse(true));
        List<InformationSchemaColumn> informationSchemaColumns = select.execute();
        table.addAll(informationSchemaColumns);
        return table;
    }

    /**
     * @param statement  statement 对象
     * @param database   数据库名
     * @param connection com.storyteller_f.easyorm_jdbc.connection 对象
     * @return 返回table的散列表
     * @throws Exception 数据库执行异常，sql-com.storyteller_f.sql_query.query 执行异常
     */
    public static HashMap<String, Table> getTables(Statement statement, String database, Connection connection) throws Exception {
        ResultSet tablesInfo = null;
        try {
            tablesInfo = statement.executeQuery("show tables;");
            HashMap<String, Table> tables_hashMap = new HashMap<>();
            ArrayList<String> tables = new ArrayList<>();
            while (tablesInfo.next()) {
                String tableName = tablesInfo.getString(1);
                tables.add(tableName);
            }
            for (String tableName : tables) {
                tables_hashMap.put(tableName, Util.geTableDetail(tableName, database, connection));
            }
            return tables_hashMap;
        } finally {
            if (tablesInfo != null) {
                if (!tablesInfo.isClosed()) {
                    tablesInfo.close();
                }
            }
        }
    }

    /**
     * @param statement statement 对象
     * @return 返回constraint 列表
     * @throws SQLException 数据库执行异常
     */
    public static ArrayList<Constraint> getConstraint(String database, Statement statement) throws SQLException {
        // c
        // t
        // r
        String constraint = String.format("SELECT C.TABLE_SCHEMA, C.REFERENCED_TABLE_NAME,C.REFERENCED_COLUMN_NAME,C.TABLE_NAME,C.COLUMN_NAME,C.CONSTRAINT_NAME,T.TABLE_COMMENT,R.UPDATE_RULE,R.DELETE_RULE " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE C " +
                "JOIN INFORMATION_SCHEMA.TABLES T ON T.TABLE_NAME = C.TABLE_NAME " +
                "JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R ON R.TABLE_NAME = C.TABLE_NAME AND R.CONSTRAINT_NAME = C.CONSTRAINT_NAME AND R.REFERENCED_TABLE_NAME = C.REFERENCED_TABLE_NAME and R.CONSTRAINT_SCHEMA='%s' " +
                "WHERE C.REFERENCED_TABLE_NAME IS NOT NULL;", database);
        ResultSet set = statement.executeQuery(constraint);
        ArrayList<Constraint> constraints = new ArrayList<>();
        while (set.next()) {
            String referenceName = set.getString(2);
            String referenceColumn = set.getString(3);
            String name = set.getString(4);
            String column = set.getString(5);
            String comment = set.getString(7);
            constraints.add(new Constraint(referenceName, referenceColumn, name, column, comment));
        }
        set.close();
        return constraints;

    }

    /**
     * 添加constraint 到相应表
     *
     * @param tables_hashMap table的散列表
     * @param constraints    需要添加的constraint
     */
    public static void addConstraintColumn(HashMap<String, Table> tables_hashMap, ArrayList<Constraint> constraints) {
        for (Constraint constraint : constraints) {
            Table temp = tables_hashMap.get(constraint.getReferenceName());
            if (temp != null) {
                //todo constraint.getName 应该找到合适的类
                temp.add(new InformationSchemaColumn(constraint.getName(), "ArrayList<" + constraint.getName() + ">", null,
                        constraint.getComment(), null, false));
            }

        }
    }

    public static void writeFile(String path, String string) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            if (file.isFile()) {
                if (!file.createNewFile()) {
                    System.out.println("create new file failure");
                }
            } else {
                if (file.isDirectory()) {
                    if (!file.mkdirs()) {
                        System.out.println("mkdirs failure");
                    }
                }
            }

        }
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(string);
        fileWriter.flush();
        fileWriter.close();
    }
}
