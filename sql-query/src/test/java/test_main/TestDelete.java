package test_main;

import com.storyteller_f.sql_query.query.Delete;
import org.junit.Test;
import test_model.User;

public class TestDelete {
    @Test
    public static void main(String[] args) {
        User user=new User();
        user.setName("yonghu");
        Delete<User> delete= new Delete<>(null);
        try {
            delete.delete(user);
            System.out.println(delete.parse(false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
