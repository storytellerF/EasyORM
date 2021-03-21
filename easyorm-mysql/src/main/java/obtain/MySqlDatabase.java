package obtain;

import org.apache.commons.text.WordUtils;
import query.expression.TwoExpression;
import query.query.ExecutableQuery;
import query.query.ExpressionQuery;
import query.result.Result;
import util.ORMUtil;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class MySqlDatabase extends Obtain {
    public static final String MYSQL_INFORMATION_SCHEMA = "INFORMATION_SCHEMA";
    private Connection connection;
    private DataSource dataSource;

    public MySqlDatabase(Connection connection) {
        super();
        this.connection = connection;
    }

    public MySqlDatabase(DataSource dataSource) {
        super();
        this.dataSource=dataSource;
    }

    @Override
    public Result getResult(ExecutableQuery<?> executableQuery)
            throws Exception {
        try {
            Class<?> returnType = getReturnType(executableQuery);
            ResultSet resultSet = getResultSet(executableQuery);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            Columns columns = getColumns(resultSetMetaData);
            Fields fields = getFields(returnType);
            DatabaseTable databaseTable = getDatabaseTable(columns, columnCount, fields);
            Result result = getResult(resultSet, columnCount, databaseTable);
            provide(returnType, fields, databaseTable, result);
            connection.close();
            return result;
        } catch (Exception e) {
            System.out.println(executableQuery.parse(true));
            throw  e;
        }

    }


    private ResultSet getResultSet(ExecutableQuery<?> executableQuery) throws Exception {
        ExpressionQuery expressionQuery = executableQuery.getExpressionQuery();
        PreparedStatement preparedStatement = getPreparedStatement(executableQuery, expressionQuery);
        return preparedStatement.executeQuery();
    }


    private Columns getColumns(ResultSetMetaData resultSetMetaData) throws SQLException {
        int count = resultSetMetaData.getColumnCount();
        Columns columns = new Columns(count);
        for (int i = 0; i < count; i++) {
            int offset = i + 1;
            String columnLabel = resultSetMetaData.getColumnLabel(offset);
            String columnTypeName = resultSetMetaData.getColumnTypeName(offset);
            columns.add(i, columnLabel, columnTypeName);
        }
        return columns;
    }

    private Result getResult(ResultSet resultSet, int columnCount, DatabaseTable databaseTable) throws NoSuchMethodException, SQLException, IllegalAccessException, InvocationTargetException {
        Result result = new Result();
        result.setCount(columnCount);
        Method[] methods = new Method[columnCount];
        for (int i1 = 0; i1 < columnCount; i1++) {
            String type1 = databaseTable.getType(i1);
            Class<?> typeClass1 = ORMUtil.getClassByType(type1);
            methods[i1] = getMethod(resultSet, i1, type1, typeClass1);
        }
        ArrayList<Object[]> arrayList = result.getData();
        while (resultSet.next()) {
            Object[] objects = new Object[columnCount];
            for (int j = 0; j < columnCount; j++) {
//                String type = databaseTable.getType(j);
                int columnIndex = j + 1;
                Object arg = methods[j].invoke(resultSet, columnIndex);
                objects[j] = arg;
//                System.out.println("type:" + type + ";result:" + arg+" result type:"+arg.getClass());
            }
            arrayList.add(objects);
        }
        /*
         * *通过反射获取resultSet的方法，调用，并存储返回值
         */
        if (!resultSet.isClosed()) {
            resultSet.close();
        }
        return result;
    }

    protected Method getMethod(ResultSet resultSet, int i, String type, Class<?> typeClass) throws NoSuchMethodException {
        String resultSetGetDataMethodName = "get" + WordUtils.capitalize(typeClass.getSimpleName());
//        System.out.println(i + ";-------type:" + type + "------method:" + resultSetGetDataMethodName);
        return resultSet.getClass().getDeclaredMethod(resultSetGetDataMethodName, int.class);
    }

    private PreparedStatement getPreparedStatement(ExecutableQuery<?> executableQuery, ExpressionQuery temp)
            throws Exception {
        PreparedStatement preparedStatement;
        if (dataSource != null) {
            connection=dataSource.getConnection();
        }
        preparedStatement=connection.prepareStatement(executableQuery.parse(true));
        int index = 1;
        while (temp != null) {
            if (temp instanceof TwoExpression<?>) {
                TwoExpression<?> twoExpression = (TwoExpression<?>) temp;
                Object class1 = twoExpression.getValue();
                if (class1 instanceof Integer) {
                    preparedStatement.setInt(index, (int) class1);
                } else if (class1 instanceof String) {
                    preparedStatement.setString(index, (String) class1);
                } else if (class1 instanceof Date) {
                    java.sql.Date date = new java.sql.Date(((Date) class1).getTime());
                    preparedStatement.setDate(index, date);
                } else {
                    throw new IllegalArgumentException("type can't be tacked:" + class1.getClass().getName());
                }
            } else {
                throw new IllegalArgumentException("未识别的类型：" + temp.getClass());
            }
            temp = temp.next();
            index++;
        }

        return preparedStatement;
    }

    @Override
    public int getInt(ExecutableQuery<?> executableQuery) throws Exception {
        try {
            PreparedStatement preparedStatement = getPreparedStatement(executableQuery,
                    executableQuery.getExpressionQuery());
            int i = preparedStatement.executeUpdate();
            connection.close();
            return i;
        } catch (Exception e) {
            System.out.println(executableQuery.parse(true));
            throw  e;
        }
    }

}
