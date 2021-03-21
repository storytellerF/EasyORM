package com.gui.test_main;

import com.gui.test_model.User;
import obtain.Plice;

import java.util.List;

public class TestHelper {
    public static void main(String[] args) {
        Plice plice=new Plice(null);
        try {
            List<User> execute = plice.getSelect(User.class).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
