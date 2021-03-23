package t;

import com.gui.main.MybatisEqualExpression;
import com.storyteller_f.sql_query.query.Select;

public class CreatorSql {
    public static void main(String[] args) throws Exception {
        Select<Users> select = new Select<>(null);
        select.select(Users.class)
                .and(new MybatisEqualExpression<>(Users.class, Users.s_name, get(Users.s_name)))
                .and(new MybatisEqualExpression<>(Users.class, Users.s_password, get(Users.s_password)));
        System.out.println(select.parse(false));
//        Insert<Users> insert=new Insert<>(null);
//        Users users=new Users();
//        insert.setObject(users).table(Users.class);
//        System.out.println(insert.parse(false));
    }

    public static String get(String u) {
        return "#{" + u + "}";
    }
}
