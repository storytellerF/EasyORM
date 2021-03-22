package com.storyteller_f.sql_query.query.mysql;

public @interface TypePercision {
	String plantform() default "mysql";
	int percision();
	int scale();
}
