package com.gui.model.validate;

public class LengthValidate extends Validate{
	
	public LengthValidate(String message, int min, int max, String shorterMessage, String longerMessage) {
		super(message);
		this.min = min;
		this.max = max;
		this.shorterMessage = shorterMessage;
		this.longerMessage = longerMessage;
	}
	private final int min;
	private final int max;
	private final String shorterMessage;
	private final String longerMessage;
	@Override
	public String parse() {
		return String.format("length:{min:%d,max:%d,shorter:'%s',longer:'%s',message:'%s'}", min,max,shorterMessage,longerMessage,message);
	}
}
