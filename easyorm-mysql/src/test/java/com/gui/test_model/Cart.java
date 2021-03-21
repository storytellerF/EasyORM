package com.gui.test_model;
import annotation.Comment;
import annotation.DefaultValue;
import annotation.RealName;
import annotation.constraint.ForeignKey;
import annotation.constraint.PrimaryKey;
import annotation.type.exact.BigIntColumn;
import annotation.type.exact.IntColumn;
import annotation.type.string.VarcharColumn;

@SuppressWarnings("unused")
@Comment(comment = "购物车")
public class Cart {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	@IntColumn
	@PrimaryKey
	@RealName(name = "购物车唯一标识")
	private int id;
	@VarcharColumn(length = 10)
	@ForeignKey(tableClass = User.class,column = "name")
	@DefaultValue("name no. 1")
	@RealName(name = "用户名")
	private String user;
	@BigIntColumn
	@RealName(name = "购物车个数")
	private int sum;
	public static String id() {
		return "id";
	}
	public static String user() {
		return "user";
	}
	public static String sum() {
		return "sum";
	}
	public static String static_id="id";
	public static String static_user="user";
	public static String static_sum="sum";
	public static String static_level="level";
}