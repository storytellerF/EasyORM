package query.mysql;

public @interface TypePercision {
	String plantform() default "mysql";
	int percision();
	int scale();
}
