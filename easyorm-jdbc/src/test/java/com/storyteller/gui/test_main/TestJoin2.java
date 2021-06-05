package com.storyteller.gui.test_main;

import com.storyteller.gui.test_model.Cart;
import com.storyteller.gui.test_model.User;
import com.storyteller_f.easyorm_jdbc.JDBCObtain;
import com.storyteller_f.sql_query.query.Select;
import com.storyteller_f.sql_query.query.column.EqualColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class TestJoin2 {

	public static void main(String[] args) {
		Connection connection;
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=Asia/Shanghai", "root", "");
			JDBCObtain jdbcObtain =new JDBCObtain(connection);


			Select<Cart> select=new Select<>(jdbcObtain);
			select.table(Cart.class).select(Cart.class);
			try {
				System.out.println(select.parse(true));
				List<Cart> execute = select.execute();
				for (Cart cart : execute) {
					System.out.println(cart);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
