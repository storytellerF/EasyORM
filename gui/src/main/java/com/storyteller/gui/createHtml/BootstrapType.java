package com.storyteller.gui.createHtml;

import java.lang.reflect.Field;

public class BootstrapType extends HTMLFormItem {
	/**
	 * 0 没有验证 如果使用的是谛听验证，一样使用0参数 
	 * 1  bootstrap 的验证方式，主要就是添加相关的class，设置submit监听事件
	 */
	private final int verifyType;
	private final String left;
	private final String right;

	public BootstrapType(int verifyType, String left,String right) {
		super();
		this.verifyType = verifyType;
		this.left = left;
		this.right = right;
	}

	@Override
	public String getInput(String name,String type,Field field) {
		return String
				.format("<input type='%s' class='form-control' id='%s' name='%s' %s>", type, name, name,needVerify()?"{verify}":"")
				.replace("{verify}", getVerification(field));
	}

	@Override
	public String getLabel(String name, String realName) {
		
		return String.format("<label for='%s' class='col-form-label %s'>%s</label>\r\n", name,left,realName);
	}

	@Override
	public String getContainer() {
		return String.format("<div class='form-group row'>\n" +
				"\t{label}\n" +
				"\t<div class='%s'>\n" +
				"\t\t{input}\n" +
				"\t</div>\n" +
				"</div>", right) ;
	}

	@Override
	public String getSelect(String name,String realName,Field field) {
		return String.format("\t<select name='%s' id='%s' %s>\r\n{option}\t</select>", name,name,needVerify()?"{verify}":"")
				.replace("{option}", getOptions(field)).replace("{verify}", getVerification(field));
	}
	@Override
	public boolean needVerify() {
		if (verifyType==1) {
			return true;
		}
		return false;
	}
	@Override
	public String parse(String name,String realName,Field field) throws Exception {
		// 此处的内容也可以完全通过getContainer 完成
		String replace = getContainer().replace("{label}", getLabel(name,realName));
		if (isSelector(field)) {
			return replace.replace("{input}", getSelect(name,realName,field));
		}
		return replace.replace("{input}", getInput(name,getInputType(field),field));
	}

}
