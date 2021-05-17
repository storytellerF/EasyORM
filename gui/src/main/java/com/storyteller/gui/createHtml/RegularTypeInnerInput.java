package com.storyteller.gui.createHtml;

public class RegularTypeInnerInput extends RegularType{

	public RegularTypeInnerInput(int verifyType) {
		super(verifyType);
	}
	@Override
	public String getLabel(String name, String realName) {
		return String.format("<label for='%s'>%s{input}</label>", name,realName);
	}
	@Override
	public String getContainer() {
		return "<div>{name}\n{label}\n</div>";
	}
}
