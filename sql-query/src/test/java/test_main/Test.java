package test_main;

import com.storyteller_f.sql_query.query.Update;
import test_model.User;

public class Test {
	@org.junit.Test
	public static void main(String[] args) {
		User user=new User();
		user.setName("yonghu");
		user.setRemark("发生了修改");
		Update<User> userUpdate=new Update<>(null);
		try {
			System.out.println(userUpdate.setObject(user).parse(false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
