package com.gui.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private ArrayList<Column> columns;
	private String tableName;

	public Table(String tableName) {
		super();
		this.tableName = tableName;
		columns = new ArrayList<>();
	}

	public void add(Column column) {
		columns.add(column);
	}
	public void addAll(List<Column> columns) {
		this.columns.addAll(columns);
	}
	public ArrayList<Column> getColumns() {
		return columns;
	}

	public String getTableName() {
		return tableName;
	}

	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
