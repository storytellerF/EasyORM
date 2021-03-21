package annotation.type.exact;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import query.mysql.TypeRange;

/**
 * numeric data
 * bytes 4
 * synonyms integer
 * @author faber
 *
 */
@TypeRange(start = "-2147483648",end = "2147483647", byteCount = 4)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntColumn {
	boolean unsinged() default false;//默认有符号
}
