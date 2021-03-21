package com.gui.model;

import annotation.Convert;
import annotation.NoQuery;

public class Column {
	@annotation.Column(name = "column_comment")
	private String comment;
	@NoQuery
	private Object data;
	@annotation.Column(name = "column_key")
	private String key;
	@annotation.Column(name = "column_name")
	private String name;
	@annotation.Column(name = "column_type")
	private String type;
	@annotation.Column(name = "is_nullable")
	@Convert(name = "nullable")
	private boolean nullable;
	public boolean nullable(String param) {
		return param.equals("YES");
	}
	
	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public Column() {
	}
	public Column(String name, String type, Object data, String comment, String key,boolean isNullable) {
		super();
		this.nullable=isNullable;
		this.setName(name);
		this.type = type;
		this.setKey(key);
		this.data = data;
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public Object getData() {
		return data;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name+":"+type;
	}
	

}
