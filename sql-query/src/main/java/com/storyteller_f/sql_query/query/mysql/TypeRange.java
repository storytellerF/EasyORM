package com.storyteller_f.sql_query.query.mysql;

public @interface TypeRange {
	String[] plantform() default "mysql";
	String start();
	String end();
	int byteCount();
}
