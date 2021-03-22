package com.gui.createHtml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.storyteller_f.sql_query.annotation.EnumRange;
import com.storyteller_f.sql_query.annotation.constraint.Nullable;
import com.storyteller_f.sql_query.annotation.type.string.EnumColumn;
import com.storyteller_f.sql_query.annotation.type.string.VarcharColumn;
import com.storyteller_f.sql_query.annotation.type.verify.Email;
import com.storyteller_f.sql_query.annotation.type.verify.Equal;
import com.storyteller_f.sql_query.annotation.type.verify.Regular;
import com.storyteller_f.sql_query.util.Util;
import com.gui.model.validate.EmialValidate;
import com.gui.model.validate.EnumValidate;
import com.gui.model.validate.EqualValidate;
import com.gui.model.validate.LengthValidate;
import com.gui.model.validate.RegularMessage;
import com.gui.model.validate.RequiredValidate;
import com.gui.model.validate.Validate;

public class DiTing {
	private final Field field;

	public DiTing(Field field) throws Exception {
		super();
		this.field = field;
		if (field.isAnnotationPresent(Email.class)) {
			validates.add(new EmialValidate("", "", ""));
		}
		boolean needAddRequired = true;
		boolean nullable = field.isAnnotationPresent(Nullable.class);
		if (field.isAnnotationPresent(Regular.class)) {
			Regular regExpression = field.getAnnotation(Regular.class);
			validates.add(new RegularMessage("", regExpression.value(), "", ""));
		}
		if (field.isAnnotationPresent(Equal.class)) {
			Equal equal = field.getAnnotation(Equal.class);
			validates.add(new EqualValidate("", equal.id(), "", ""));
		}
		if (field.isAnnotationPresent(EnumRange.class)) {
			EnumRange enumRange=field.getAnnotation(EnumRange.class);
			int count=enumRange.end()-enumRange.start()+1;
			String[] strings=new String[count];
			for (int i = 0; i < strings.length; i++) {
				strings[i]=enumRange.before()+enumRange.start()+i;
			}
			validates.add(new EnumValidate("", strings, ""));
		}
		if (field.isAnnotationPresent(EnumColumn.class)) {
			EnumColumn enumColumn=field.getAnnotation(EnumColumn.class);
			validates.add(new EnumValidate("", enumColumn.value(), ""));
		}
		Annotation annotation = Util.getColumnAnnotation(field);
		if (annotation == null) {
			throw new Exception("无法获取字段类型，未通过字段设置类型");
		}
		switch (annotation.annotationType().getSimpleName()) {
		case "VarcharColumn":
			VarcharColumn varcharColumn = field.getAnnotation(VarcharColumn.class);
			needAddRequired = false;
			validates.add(new LengthValidate("", nullable ? 0 : 1, varcharColumn.length(), "", ""));
			break;

		default:
			break;
		}
		if (nullable && needAddRequired) {
			validates.add(new RequiredValidate("", "", ""));
		}
	}

	private final List<Validate> validates = new ArrayList<Validate>();

	public String parse() {
		StringBuilder stringBuilder = new StringBuilder("\t\t"+field.getName()+":{\n");
		for (Validate validate : validates) {
			stringBuilder.append("\t\t").append(validate.parse()).append("\n\t},\n");
		}
		stringBuilder.append("}\n");
		return stringBuilder.toString();
	}
}
