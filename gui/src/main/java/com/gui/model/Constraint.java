package com.gui.model;

public class Constraint {
	private String column;
	private String comment;
	private String name;
	private String referenceColumn;
	private String referenceName;

	public Constraint(String referenceName, String referenceColumn, String name, String column, String comment) {
		super();
		this.referenceName = referenceName;
		this.referenceColumn = referenceColumn;
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

	public String getReferenceColumn() {
		return referenceColumn;
	}

	public String getReferenceName() {
		return referenceName;
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

	public void setReferenceColumn(String referenceColumn) {
		this.referenceColumn = referenceColumn;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

}
