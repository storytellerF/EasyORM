package com.gui.test_main;

import com.gui.test_model.User;
import com.storyteller_f.sql_query.query.Delete;

public class TestDelete {
    public static void main(String[] args) {
        User user=new User();
        user.setName("yonghu");
        Delete<User> delete=new Delete<User>(null);
        try {
            delete.delete(user);
            System.out.println(delete.parse(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
