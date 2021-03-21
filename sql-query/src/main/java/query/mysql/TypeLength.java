package query.mysql;

public @interface TypeLength {
	String plantform() default "mysql";
	int length();
}
