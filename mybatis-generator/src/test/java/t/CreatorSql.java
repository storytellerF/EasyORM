package t;

import com.gui.main.NewEqual;
import com.storyteller_f.sql_query.query.Select;

public class CreatorSql {
    public static void main(String[] args) throws Exception {
        Select<Users> select = new Select<>(null);
        select.select(Users.class)
                .and(new NewEqual<>(Users.class, static_columns.Users.name, get(static_columns.Users.name)))
                .and(new NewEqual<>(Users.class, static_columns.Users.password, get(static_columns.Users.password)));
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
