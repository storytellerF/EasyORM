package test_model;

import annotation.Column;

public class UserSlim {
	@Column(name = "name")
	private String name;
	@Column(name = "age")
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	public static String static_name="name";
	public static String static_age="age";
}
