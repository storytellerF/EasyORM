package com.gui.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.gui.model.Column;
import com.gui.model.Table;
import org.apache.commons.text.CaseUtils;

import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.annotation.constraint.Unique;
import com.gui.model.validate.CustomField;

public class ParseDatabase {
    private final static String tag = "ParseDatabase";
    private final String packageStr;
    private final String path;
    private final HashMap<String, Table> tables;

    public ParseDatabase(CreateConfig config) {
        super();
        this.path = config.getPath();
        this.packageStr = config.getPackageStr();
        this.tables = config.getTables();
    }

    public String getModelClass(String packageStr, String className, ArrayList<Column> columns, boolean enableLombok) throws Exception {
        System.out.println(tag + "getModelClass method called");
        System.out.println(tag + packageStr + ";" + className);
        ColumnsToField columnsToField = new ColumnsToField(packageStr, className);
        for (Column column : columns) {
            String nameValue = column.getName();
            String typeValue = column.getType();
            System.out.println(tag + "name:" + nameValue + " type:" + typeValue + "key:" + column.getKey());
            CustomField customField = new CustomField();
            customField.nameAndType(nameValue, typeValue);
            if (column.isNullable()) {
                customField.add("@Nullable");
            }
            if (column.getKey() == null) {
                //todo 依赖问题
            } else if (column.getKey().equals("PRI")) {
                customField.add("@PrimaryKey");
                customField.add(PrimaryKey.class);
            } else if (column.getKey().equals("UNI")) {
                customField.add("@Unique");
                customField.add(Unique.class);
            }
            StringBuilder append = new StringBuilder();
            if (enableLombok)
                append.append("\tpublic void set").append(CaseUtils.toCamelCase(nameValue, true)).append("(").append(customField.getType()).append(" ").append(nameValue).append("){\n")
                        .append("\t\tthis.").append(nameValue).append("=").append(nameValue).append(";\n")
                        .append("\t}\n")
                        .append("\tpublic ").append(customField.getType()).append(" get").append(CaseUtils.toCamelCase(nameValue, true)).append("(){\n")
                        .append("\t\treturn ").append(nameValue).append(";\n")
                        .append("\t}\n");

            append.append("\tpublic static String ").append(nameValue).append("(){\n").append("\t\treturn ").append(nameValue).append("\n\t}\n");
            columnsToField.addMethod(append.toString());
//			customField.add("@RealName( name =\"" + real.getStringCellValue() + "\")");
            columnsToField.add(customField);
        }
        return columnsToField.content();
    }

    public void parseDatabase(boolean enableLombok) throws Exception {
        for (Entry<String, Table> iterable : tables.entrySet()) {
            String key = iterable.getKey();
            Table value = iterable.getValue();
            ArrayList<Column> columns = value.getColumns();
//			System.out.println("name:"+key+" to:"+CaseUtils.toCamelCase(key,true));
            String path = Paths.get(this.path, CaseUtils.toCamelCase(key, true) + ".java").toString();
//			writeFile(path, getModelClass(packageStr, key, columns));
            System.out.println(getModelClass(packageStr, key, columns, enableLombok));
        }
    }

    private void writeFile(String path, String string) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            if (file.isFile()) {
                if (!file.createNewFile()) {
                    System.out.println("create new file failure");
                }
            } else {
                if (file.isDirectory()) {
                    if (!file.mkdirs()) {
                        System.out.println("mkdirs failure");
                    }
                }
            }

        }
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(string);
        fileWriter.flush();
        fileWriter.close();
    }
}
