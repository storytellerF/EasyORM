package com.gui.model.validate;

public class EqualValidate extends Validate{
	private final String id;
	private final String equalMessage;
	
	public EqualValidate(String message, String id, String equalMessage, String notEqualMessage) {
		super(message);
		this.id = id;
		this.equalMessage = equalMessage;
		this.notEqualMessage = notEqualMessage;
	}
	private final String notEqualMessage;
	@Override
	public String parse() {
		return String.format("equal:{id:'%s',equal:'%s',notEqual:'%s',message:'%s'}", id,equalMessage,notEqualMessage,message);
	}
}
