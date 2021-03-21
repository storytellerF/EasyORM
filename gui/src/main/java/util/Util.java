package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gui.model.Column;
import com.gui.model.Constraint;
import com.gui.model.Table;
import query.Select;
import query.expression.EqualExpression;
import obtain.MySqlDatabase;

public class Util {
    /**
     * 获取第一个结尾是Column的注解，根据设定，结尾是Column的注解是数据库中的字段类型
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

    public static boolean testConnection(String url, String user, String password) {
        try {
            DriverManager.getConnection(url, user, password).close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
     * @param tableName 查询的表名
     * @param database 查询的数据库名
     * @param connection 用来进行查询的connection 对象
     * @return table 返回table对象
     * @throws Exception sql-query 执行异常
     */
    public static Table geTable(String tableName, String database, Connection connection) throws Exception {
        System.out.println("name:" + tableName + ";database:" + database);
        Table table;
        table = new Table(tableName);
        MySqlDatabase mySqlDatabase = new MySqlDatabase(connection);
        Select<Column> select = new Select<>(mySqlDatabase);
        select.select(Column.class).table(MySqlDatabase.MYSQL_INFORMATION_SCHEMA + "`.Columns`")
                .and(new EqualExpression<>(null, "table_name", tableName).next(new EqualExpression<>(null, "table_schema", database)));
        System.out.println(select.parse(true));
        List<Column> columns = select.execute();
        table.addAll(columns);

        return table;

    }

    /**
     * @param statement statement 对象
     * @param database 数据库名
     * @param connection connection 对象
     * @return 返回table的散列表
     * @throws Exception 数据库执行异常，sql-query 执行异常
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
                tables_hashMap.put(tableName, Util.geTable(tableName, database, connection));
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
        String constraint = "SELECT C.TABLE_SCHEMA, C.REFERENCED_TABLE_NAME,"
                + "C.REFERENCED_COLUMN_NAME,C.TABLE_NAME,"
                + "C.COLUMN_NAME,C.CONSTRAINT_NAME,"
                + "T.TABLE_COMMENT,R.UPDATE_RULE,"
                + "R.DELETE_RULE FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE C" + // c
                " JOIN INFORMATION_SCHEMA.TABLES T" + // t
                " ON T.TABLE_NAME = C.TABLE_NAME" + "" +
                " JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R" + // r
                " ON R.TABLE_NAME = C.TABLE_NAME" + " AND R.CONSTRAINT_NAME = C.CONSTRAINT_NAME "
                + "AND R.REFERENCED_TABLE_NAME = C.REFERENCED_TABLE_NAME and R.CONSTRAINT_SCHEMA=" + "'" + database + "' "
                + "WHERE C.REFERENCED_TABLE_NAME IS NOT NULL;";
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
     * @param tables_hashMap table的散列表
     * @param constraints 需要添加的constraint
     */
    public static void addConstraintColumn(HashMap<String, Table> tables_hashMap, ArrayList<Constraint> constraints) {
        for (Constraint constraint : constraints) {
            Table temp = tables_hashMap.get(constraint.getReferencename());
            if (temp != null) {
                //todo constraint.getName 应该找到合适的类
                temp.add(new Column(constraint.getName(), "ArrayList<" + constraint.getName() + ">", null,
                        constraint.getComment(), null, false));
            }

        }
    }
}
