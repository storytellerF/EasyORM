package com.storyteller.gui.model.validate;

public class RegularMessage extends Validate{
	private final String regExpression;
	public RegularMessage(String message, String regExpression, String correctMessage, String errorMessage) {
		super(message);
		this.regExpression = regExpression;
		this.correctMessage = correctMessage;
		this.errorMessage = errorMessage;
	}
	private final String correctMessage;
	private final String errorMessage;
	@Override
	public String parse() {
		return String.format("regular:{reg:'%s',correct:'%s',error:'%s',message:'%s'}",regExpression,correctMessage,errorMessage,message);
	}

}
