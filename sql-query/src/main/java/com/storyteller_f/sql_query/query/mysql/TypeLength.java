package com.storyteller_f.sql_query.query.mysql;

public @interface TypeLength {
	String plantform() default "mysql";
	int length();
}
