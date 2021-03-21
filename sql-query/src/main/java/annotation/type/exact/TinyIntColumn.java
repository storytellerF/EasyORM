package annotation.type.exact;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import query.mysql.TypeRange;
@TypeRange(start = "",end = "",byteCount = 1)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TinyIntColumn {
	boolean unsinged() default false;//默认无符号
}
