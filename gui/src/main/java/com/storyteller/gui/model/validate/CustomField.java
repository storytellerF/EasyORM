package com.storyteller.gui.model.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.storyteller_f.sql_query.annotation.type.exact.IntColumn;
import com.storyteller_f.sql_query.annotation.type.exact.TinyIntColumn;
import com.storyteller_f.sql_query.annotation.type.fixed_point.DecimalColumn;
import com.storyteller_f.sql_query.annotation.type.string.CharColumn;
import com.storyteller_f.sql_query.annotation.type.string.VarcharColumn;
import com.storyteller_f.sql_query.exception.UnexpectedTypeException;
import com.storyteller.util.Util;

public class CustomField {
    private String name;
    private String type;
    private final List<Class<?>> importClass = new ArrayList<>();
    private final List<String> annotationList = new ArrayList<>();
    private final HashMap<String, String> enumHashMap = new HashMap<>();

    public String produceEnumString() {
        HashMap<String, String> hashMap = getEnumHashMap();
        if (!hashMap.isEmpty()) {
            Iterator<Entry<String, String>> iterator2 = hashMap.entrySet().iterator();
            StringBuilder enumColumn = new StringBuilder();
            enumColumn.append("\t@EnumColumn({");
            StringBuilder enumRemark = new StringBuilder();
            enumRemark.append("\t@EnumRemark({");
            while (iterator2.hasNext()) {
                Entry<String, String> entry = iterator2
                        .next();
                enumColumn.append("\"").append(entry.getKey()).append("\",");
                enumRemark.append("\"").append(entry.getValue()).append("\",");
            }
            enumColumn.deleteCharAt(enumColumn.length() - 1);
            enumRemark.deleteCharAt(enumRemark.length() - 1);
            enumColumn.append("})\n");
            enumRemark.append("})\n");
            return enumColumn.toString() + enumRemark.toString();
        }
        return "";
    }

    public void add(String annotation) {
        annotationList.add(annotation);
    }

    public Iterator<String> getAnnotationIterator() {
        return annotationList.iterator();
    }

    public HashMap<String, String> getEnumHashMap() {
        return enumHashMap;
    }

    public void put(String key, String value) {
        enumHashMap.put(key, value);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void add(Class<?> importClass) {
        this.importClass.add(importClass);
    }

    public List<Class<?>> getImportClass() {
        return importClass;
    }


    public void nameAndType(String name, String type) throws Exception {
        this.name = name;

        if (type.toLowerCase().startsWith("varchar")) {
            int number = Util.getNumber(type);
            if (number == 1) {
                setType("char");//这种情况基本不存在
            } else {
                add("@VarcharColumn( length = " + number + " )");
                setType("String");
                importClass.add(VarcharColumn.class);
            }
        } else if (type.toLowerCase().startsWith("char")) {
            int number = Util.getNumber(type);
            if (number == 1) {
                setType("char");
            } else {
                add("@CharColumn( length = " + number + " )");
                setType("String");
                importClass.add(CharColumn.class);
            }
        } else if (type.toLowerCase().startsWith("int")) {
            add("@IntColumn");
            setType("int");
            importClass.add(IntColumn.class);
        } else if (type.toLowerCase().startsWith("decimal")) {
            int[] two = Util.getTwo(type);
            add("@DecimalColumn( exact = " + two[0] + ", approximate= " + two[1] + " )");
            setType("double");
            importClass.add(DecimalColumn.class);
        } else if (type.toLowerCase().startsWith("tinyint")) {
            add("@TinyIntColumn");
            setType("int");
            importClass.add(TinyIntColumn.class);
        } else if (type.contains("ArrayList<")) {
            //todo 处理依赖问题
        } else {
            throw new UnexpectedTypeException("不支持的类型" + type);
        }
    }

    private void setType(String string) {
        this.type = string;
    }

}
