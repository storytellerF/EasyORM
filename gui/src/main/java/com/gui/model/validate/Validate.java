package com.gui.model.validate;

public abstract class Validate {
	public Validate(String message) {
		this.message=message;
	}

	protected String message;
	public abstract String parse();
}
