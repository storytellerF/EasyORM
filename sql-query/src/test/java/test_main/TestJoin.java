package test_main;

import query.Select;
import query.column.EqualColumn;
import test_model.Cart;
import test_model.User;
import test_model.UserSlim;

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
