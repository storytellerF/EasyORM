package com.storyteller.gui.model;

import com.storyteller_f.sql_query.annotation.Column;
import com.storyteller_f.sql_query.annotation.Convert;
import com.storyteller_f.sql_query.annotation.NoQuery;
import com.storyteller_f.sql_query.annotation.Table;

@Table(name = "Column")
public class InformationSchemaColumn {
	@Column(name = "column_comment")
	private String comment;
	@NoQuery
	private Object data;
	@Column(name = "column_key")
	private String key;
	@Column(name = "column_name")
	private String name;
	@Column(name = "column_type")
	private String type;
	@Column(name = "is_nullable")
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
	public InformationSchemaColumn() {
	}
	public InformationSchemaColumn(String name, String type, Object data, String comment, String key, boolean isNullable) {
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
