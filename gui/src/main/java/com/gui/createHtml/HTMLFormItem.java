package com.gui.createHtml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import annotation.EnumRange;
import annotation.EnumRemark;
import annotation.constraint.Nullable;
import annotation.type.string.EnumColumn;

public abstract class HTMLFormItem {

	public HTMLFormItem() {
		super();
	}

	public abstract String getInput(String name, String type, Field field);

	public abstract String getLabel(String name, String realName);

	public abstract String getSelect(String name, String realName, Field field);

	public abstract String getContainer();

	public boolean isSelector(Field field) {
		return field.isAnnotationPresent(EnumColumn.class) || field.isAnnotationPresent(EnumRange.class);
	}

	public String getOptions(Field field) {
		EnumRemark enumRemark = null;
		if (field.isAnnotationPresent(EnumRemark.class)) {
			enumRemark = field.getAnnotation(EnumRemark.class);
		}
		StringBuilder stringBuilder = new StringBuilder();
		if (field.isAnnotationPresent(EnumColumn.class)) {
			EnumColumn enumColumn = field.getAnnotation(EnumColumn.class);
			String[] c = enumColumn.value();
			int i = 0;
			for (String str : c) {
				String key;
				if (enumRemark != null) {
					key = enumRemark.value()[i];
				} else {
					key = str;
				}
				stringBuilder.append("\t\t").append(getOption(str, key)).append("\n");
				i++;
			}
		}
		if (field.isAnnotationPresent(EnumRange.class)) {
			EnumRange enumRange = field.getAnnotation(EnumRange.class);
			int i = 0;
			char temp = enumRange.start();
			while (temp < enumRange.end()) {
				String key;
				String value = enumRange.before() + temp;
				if (enumRemark != null) {
					key = enumRemark.value()[i];
				} else {
					key = value;
				}
				stringBuilder.append("\t\t").append(getOption(enumRange.before() + temp, key)).append("\n");
				i++;
				temp++;
			}

		}
		return stringBuilder.toString();
	}

	public String getOption(String value, String key) {
		return "\t<option value='" + value + "'>" + key + "</option>";
	}

	public String getScript(String id) {
		return String.format("var %s = document.getElementById('%s');", id, id);
	}

	public abstract String parse(String name, String realName, Field field)  throws Exception;
	public abstract boolean needVerify() ;
	public String getVerification(Field field) {
		if (!needVerify()) {
			return "";
		}
		if (!field.isAnnotationPresent(Nullable.class)) {
			return "required";
		}
		return "";
	}

	private Annotation getColumnAnnotation(Field field) {
		Annotation[] annotations = field.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().getSimpleName().endsWith("Column")) {
				return annotation;
			}
		}
		return null;
	}
	public String getDiTingScript() {
		return "";
	}
	public String getInputType(Field field) throws Exception {
		String type;
		Annotation annotation = getColumnAnnotation(field);
		if (annotation != null) {
			String annotationName = annotation.annotationType().getName();
			switch (annotationName) {
			case "annotation.type.exact.BigIntColumn":
			case "annotation.type.exact.MediumIntColumn":
			case "annotation.type.exact.TinyIntColumn":
			case "annotation.type.exact.SmallIntColumn":
			case "annotation.type.exact.DecimalColumn":
			case "annotation.type.exact.NumericColumn":
			case "annotation.type.exact.IntColumn":
				type = "number";
				break;
			case "annotation.type.string.TextColumn":
			case "annotation.type.string.CharColumn":
			case "annotation.type.string.VarcharColumn":
				type = "text";
				break;
			case "annotation.type.date.DateColumn":
				type = "date";
				break;
//                case "annotation.type.string.BinaryColumn":
//                    return "binary("+((BinaryColumn)annotation).length()+")";
//                case "annotation.type.string.BlobColumn":
//                    return "blog("+((BlobColumn)annotation).length()+")";
//                case "annotation.type.string.VarbinaryColumn":
//                    return "varbinary("+((VarbinaryColumn)annotation).length()+")";
			default:
				throw new Exception("当前类型不受支持:" + annotationName);
			}
			return type;
		} else {
			switch (field.getType().toString()) {
			case "int":
				type = "number";
				break;
			case "double":
				type = "text";
				break;
			case "class java.lang.String":
				type = "text";
				break;
			default:
				throw new Exception("当前类型不受支持:" + field.getType().toString());

			}
			return type;
		}
	}

}
