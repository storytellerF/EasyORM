package com.gui.test_main;

import com.gui.test_model.Cart;
import com.gui.test_model.User;
import obtain.MySqlDatabase;
import query.Select;
import query.column.EqualColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class TestJoin {

	public static void main(String[] args) {
		Connection connection;
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=Asia/Shanghai", "root", "");
			MySqlDatabase mySqlDatabase=new MySqlDatabase(connection);
			Select<User> select=new Select<>(mySqlDatabase);
			select.table(User.class).select(User.class).leftJoin(Cart.class,
					new EqualColumn(User.class,User.name(),Cart.class,Cart.user())
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