package test_main;


import java.rmi.UnexpectedException;

import query.Create;
import test_model.Cart;


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
