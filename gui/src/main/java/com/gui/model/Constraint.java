package com.gui.model;

public class Constraint {
	private String column;
	private String comment;
	private String name;
	private String referencecolumn;
	private String referencename;

	public Constraint(String referencename, String referencecolumn, String name, String column, String comment) {
		super();
		this.referencename = referencename;
		this.referencecolumn = referencecolumn;
		this.name = name;
		this.column = column;
		this.setComment(comment);
	}

	public String getColumn() {
		return column;
	}

	public String getComment() {
		return comment;
	}

	public String getName() {
		return name;
	}

	public String getReferencecolumn() {
		return referencecolumn;
	}

	public String getReferencename() {
		return referencename;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReferencecolumn(String referencecolumn) {
		this.referencecolumn = referencecolumn;
	}

	public void setReferencename(String referencename) {
		this.referencename = referencename;
	}

}
