package t;

import com.storyteller.gui.model.Users;
import com.storyteller_f.sql_query.query.Select;
import com.storyteller_f.sql_query.query.expression.EqualExpression;

public class CreatorSql {
    public static void main(String[] args) throws Exception {
        Select<Users> select = new Select<>(null);
        select.select(Users.class)
                .and(new EqualExpression<>(Users.class, static_columns.Users.name, get(static_columns.Users.name)))
                .and(new EqualExpression<>(Users.class, static_columns.Users.password, get(static_columns.Users.password)));
        System.out.println(select.parse(false));
    }

    public static String get(String u) {
        return "#{" + u + "}";
    }
}
