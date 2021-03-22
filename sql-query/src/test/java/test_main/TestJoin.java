package test_main;

import com.storyteller_f.sql_query.query.Select;
import com.storyteller_f.sql_query.query.column.EqualColumn;
import test_model.Cart;
import test_model.User;

public class TestJoin {

	public static void main(String[] args) {
		Select<User> select=new Select<>(null);
		select.table(User.class).select(User.class).leftJoin(Cart.class,
				new EqualColumn(User.class,User.name(),Cart.class,Cart.user())
				);
		try {
			System.out.println(select.parse(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
