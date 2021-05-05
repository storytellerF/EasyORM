import com.storyteller_f.easyorm_jdbc.JDBCObtain;
import com.storyteller_f.easyorm_search.Pager;
import com.storyteller_f.sql_query.query.Select;
import com.storyteller_f.sql_query.query.query.ExpressionQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=Asia/Shanghai", "root", "");
            JDBCObtain obtain =new JDBCObtain(connection);
//            obtain.setAutoCloseConnection();
            Pager pager=new Pager(5,1,obtain);
            List<User> execute = pager.execute(User.class);
            for (User user : execute) {
                System.out.println(user);
            }
            UserSearch searchPattern=new UserSearch();
            searchPattern.setAge(3);
            searchPattern.setName("u");
            ExpressionQuery[] parse = searchPattern.parse();
            Select<User> and = obtain.getSelect(User.class).and(parse);
//            obtain.disableTransaction();
            System.out.println(and.parse(false));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
