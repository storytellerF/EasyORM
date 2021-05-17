package com.storyteller.gui.test_main;

import com.storyteller.gui.test_model.Cart;
import com.storyteller.gui.test_model.User;
import com.storyteller_f.easyorm_jdbc.JDBCObtain;
import com.storyteller_f.sql_query.query.Select;
import com.storyteller_f.sql_query.query.column.EqualColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class TestJoin {

	public static void main(String[] args) {
		Connection connection;
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=Asia/Shanghai", "root", "");
			JDBCObtain jdbcObtain =new JDBCObtain(connection);
			Select<User> select=new Select<>(jdbcObtain);
			select.table(User.class).select(User.class).leftJoin(Cart.class,
					new EqualColumn(User.class,User::name,Cart.class,Cart::user)
			);
			try {
				System.out.println(select.parse(true));
				List<User> execute = select.execute();
				for (User user : execute) {
					System.out.println(user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
