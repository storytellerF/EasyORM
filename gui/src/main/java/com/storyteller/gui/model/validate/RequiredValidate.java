package com.storyteller.gui.model.validate;

public class RequiredValidate extends Validate{
	
	public RequiredValidate(String message, String emptyMessage, String notEmptyMessage) {
		super(message);
		this.emptyMessage = emptyMessage;
		this.notEmptyMessage = notEmptyMessage;
	}
	private final String emptyMessage;
	private final String notEmptyMessage;
	@Override
	public String parse() {
		return String.format("required:{empty:'%s',notEmpty:'%s',message:'%s'}", emptyMessage,notEmptyMessage,message);
	}
}
