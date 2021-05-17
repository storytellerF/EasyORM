package com.storyteller.gui.model.validate;

public class RangeValidate extends Validate {
	private final int maxValue;
	private final int minValue;
	private final String greaterThanMessage;
	private final String lessThanMessage;
	public RangeValidate(String message, int maxValue, int minValue, String greaterThanMessage,
			String lessThanMessage) {
		super(message);
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.greaterThanMessage = greaterThanMessage;
		this.lessThanMessage = lessThanMessage;
	}
	@Override
	public String parse() {
		return String.format("range:{min:%d,max:%d,greater:'%s',less:'%s,message:'%s''}", minValue,maxValue,greaterThanMessage,lessThanMessage,message);
	}
}
