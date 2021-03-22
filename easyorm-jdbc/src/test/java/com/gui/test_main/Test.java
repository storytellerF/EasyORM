package com.gui.test_main;

import com.gui.test_model.User;
import query.Update;

public class Test {
	public static void main(String[] args) {
		User user=new User();
		user.setName("yonghu");
		user.setRemark("发生了修改");
		Update<User> userUpdate=new Update<>(null);
		try {
			System.out.println(userUpdate.setObject(user).parse(true));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
