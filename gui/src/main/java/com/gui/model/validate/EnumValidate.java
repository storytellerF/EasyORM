package com.gui.model.validate;

import java.util.Arrays;

public class EnumValidate extends Validate{
	private final String[] enumList;
	private final String correctMessage;
	public EnumValidate(String message, String[] enumList, String correctMessage) {
		super(message);
		this.enumList = enumList;
		this.correctMessage = correctMessage;
	}
	private String errorMessage;
	@Override
	public String parse() {
		return String.format("enum:{list:%s,correct:'%s',error:'%s',message:'%s'}", Arrays.toString(enumList),correctMessage,errorMessage,message);
	}
}
