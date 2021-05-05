package com.gui.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String tableName;
	/**
	 * 表的所有列
	 */
	private ArrayList<InformationSchemaColumn> informationSchemaColumns;

	public Table(String tableName) {
		super();
		this.tableName = tableName;
		informationSchemaColumns = new ArrayList<>();
	}

	public void add(InformationSchemaColumn informationSchemaColumn) {
		informationSchemaColumns.add(informationSchemaColumn);
	}
	public void addAll(List<InformationSchemaColumn> informationSchemaColumns) {
		this.informationSchemaColumns.addAll(informationSchemaColumns);
	}
	public ArrayList<InformationSchemaColumn> getColumns() {
		return informationSchemaColumns;
	}

	public String getTableName() {
		return tableName;
	}

	public void setColumns(ArrayList<InformationSchemaColumn> informationSchemaColumns) {
		this.informationSchemaColumns = informationSchemaColumns;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
