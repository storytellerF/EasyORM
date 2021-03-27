package com.gui.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gui.model.validate.CustomField;
import org.apache.commons.text.CaseUtils;

/**
 * 用于创建class文件，实现存储好字段名称，类名，包名，方法名
 */
public class ColumnsToField {
	private final List<CustomField> customFields= new ArrayList<>();
	private final String packageName;
	private final String name;
	public ColumnsToField(String packageStr,String name) {
		packageName=packageStr;
		this.name=name;
	}
	private final List<String> methodList= new ArrayList<>();
	public void add(CustomField customField) {
		customFields.add(customField);
	}
	public void addMethod(String method) {
		methodList.add(method);
	}
	public String content() {
		
		StringBuilder classContent = new StringBuilder("package ").append(packageName).append(";\n");
		int packageLength=classContent.length();
		classContent.append("public class ").append(CaseUtils.toCamelCase(name,true)).append("{\n");
		StringBuilder importClass=new StringBuilder();
		List<Class<?>> ed = new ArrayList<>();
		for (CustomField customField : customFields) {
			for (Class<?> class1: customField.getImportClass()) {//获取需要导入的包
				if (!ed.contains(class1)) {//如果已经存在了，就不添加了
					ed.add(class1);
				}
			}
			Iterator<String> annotationIterator = customField.getAnnotationIterator();
			while (annotationIterator.hasNext()) {
				classContent.append("\t").append(annotationIterator.next()).append("\n");
			}
			classContent.append(customField.produceEnumString());
			String name = customField.getName();
			classContent.append("\tprivate ").append(customField.getType()).append(" ")
					.append(name).append(";\n");
//			classContent.append("\t@NoQuery\n").append("\tpublic static String column_").append(name).append("=\"").append(name).append("\";\n");
		}
		//生成导入类数据
		for (Class<?> class1 : ed) {
			importClass.append("import ").append(class1.getName()).append(";\n");
		}
		classContent.insert(packageLength,importClass.toString());
		for (String string : methodList) {
			classContent.append(string).append("\n");
		}
		classContent.append("}");
		return classContent.toString();
	}
}
