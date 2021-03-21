package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用来定义列名，如果存储到表中的列名与字段名不同，必须添加此注解，以免出现错误
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name();
}
