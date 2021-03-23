package com.gui.model;

import com.config_editor.model.Config;

public class ExcelReadConfig extends Config {
    private String path;
    private String sheetName;
    private int columnCount;
    private int rowCount;
    private int tableHeaderRow;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getTableHeaderRow() {
        return tableHeaderRow;
    }

    public void setTableHeaderRow(int tableHeaderRow) {
        this.tableHeaderRow = tableHeaderRow;
    }

    @Override
    public Object clone() {
        ExcelReadConfig excelReadConfig = new ExcelReadConfig();
        excelReadConfig.setPath(path);
        excelReadConfig.setColumnCount(columnCount);
        excelReadConfig.setRowCount(rowCount);
        excelReadConfig.setTableHeaderRow(tableHeaderRow);
        excelReadConfig.setSheetName(sheetName);
        return excelReadConfig;
    }
}
