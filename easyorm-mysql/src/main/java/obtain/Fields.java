package obtain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Fields {
    /*
     * 获取所有列的真实名字，相当于提前获取
     */
    List<String> columnNamesInField = new ArrayList<>();
    /*
     * *存储带有@Column 注解的字段
     */
    List<Field> fieldsWithColumnAnnotationList = new ArrayList<>();

    public void add(String name, Field field) {
        columnNamesInField.add(name);
        fieldsWithColumnAnnotationList.add(field);
    }

    public Field getField(int index) {
        return fieldsWithColumnAnnotationList.get(index);
    }

    public String getName(int index) {
        return columnNamesInField.get(index);
    }

    public int getCount() {
        return  columnNamesInField.size();
    }

}
