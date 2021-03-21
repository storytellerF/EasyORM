package query;

public class Value {
    public String objectToString(Object value) {
        if (value==null) return "";
        if (value instanceof String) {
            return "'" + value + "'";
        } else if (value instanceof Integer) {
            return String.valueOf(value);
        }
        throw new IllegalArgumentException("不支持的类型:" + value.getClass());
    }
}
