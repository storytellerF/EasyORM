package com.storyteller.gui.main;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.storyteller.gui.model.InformationSchemaColumn;
import com.storyteller.gui.model.Table;
import com.storyteller.util.Util;
import org.apache.commons.text.CaseUtils;

import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.annotation.constraint.Unique;
import com.storyteller.gui.model.validate.CustomField;

public class ParseDatabase {
    private final static String tag = "ParseDatabase";
    private final String packageName;
    private final String path;
    private final HashMap<String, Table> tables;

    public ParseDatabase(TableConfig config) {
        super();
        this.path = config.getConfig().getPath();
        this.packageName = config.getConfig().getPackageStr();
        this.tables = config.getTables();
    }

    public String getModelClass(String packageStr, String className, ArrayList<InformationSchemaColumn> informationSchemaColumns, boolean enableLombok) throws Exception {
        System.out.println(tag + "getModelClass method called");
        System.out.println(tag + packageStr + ";" + className);
        ColumnsToField columnsToField = new ColumnsToField(packageStr, className);
        for (InformationSchemaColumn informationSchemaColumn : informationSchemaColumns) {
            String nameValue = informationSchemaColumn.getName();
            String typeValue = informationSchemaColumn.getType();
            System.out.println(tag + "name:" + nameValue + " type:" + typeValue + "key:" + informationSchemaColumn.getKey());
            CustomField customField = new CustomField();
            customField.nameAndType(nameValue, typeValue);
            if (informationSchemaColumn.isNullable()) {
                customField.add("@Nullable");
            }
            if (informationSchemaColumn.getKey() == null) {
                //todo 依赖问题
            } else if (informationSchemaColumn.getKey().equals("PRI")) {
                customField.add("@PrimaryKey");
                customField.add(PrimaryKey.class);
            } else if (informationSchemaColumn.getKey().equals("UNI")) {
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

            append.append("\tpublic static String ").append(nameValue).append("(){\n").append("\t\treturn \"").append(nameValue).append("\";\n\t}\n");
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
            ArrayList<InformationSchemaColumn> informationSchemaColumns = value.getColumns();
//			System.out.println("name:"+key+" to:"+CaseUtils.toCamelCase(key,true));
            String javaFileName = CaseUtils.toCamelCase(key, true);
            String path = Paths.get(this.path, javaFileName + ".java").toString();
            String modelClass = getModelClass(packageName, key, informationSchemaColumns, enableLombok);
            Util.writeFile(path, modelClass);
            System.out.println(modelClass);
        }
    }


}
