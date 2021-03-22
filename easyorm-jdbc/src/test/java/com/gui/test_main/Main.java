package com.gui.test_main;


import com.gui.test_model.Cart;
import com.storyteller_f.sql_query.query.Create;

import java.rmi.UnexpectedException;


public class Main {

	public static void main(String[] args) {
		Create name = new Create(Cart.class);
		try {
			System.out.println(name.parse(true));
		} catch (UnexpectedException e) {
			e.printStackTrace();
		}
	}

}
