package com.gui.model.validate;

public class EmialValidate extends RegularMessage {

	public EmialValidate(String message, String correctMessage, String errorMessage) {
		super(message, "/[\\w]+@(?:[\\dA-Za-z-]+\\.)+[a-zA-Z]{2,4}/", correctMessage, errorMessage);
	}

}
