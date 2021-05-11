package test_main;


import java.rmi.UnexpectedException;

import com.storyteller_f.sql_query.query.Create;
import org.junit.Test;
import test_model.Cart;

public class Main {
	@Test
	public void main() {
		Create name = new Create(null,Cart.class);
		try {
			System.out.println(name.parse(false));
		} catch (UnexpectedException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testCache() {

	}

}
