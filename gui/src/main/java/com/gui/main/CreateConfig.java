package com.gui.main;

import com.gui.model.Constraint;
import com.gui.model.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateConfig {
	private final String packageStr;
	private final String path;
	private final String database;
	private final HashMap<String, Table> tables;
	private final ArrayList<Constraint> constraints;
	private final String url;
	private final String user;
	private final String password;

	public CreateConfig(String packageStr, String path, String database, HashMap<String, Table> tables,
			ArrayList<Constraint> constraints, String url, String user, String password) {
		super();
		this.packageStr = packageStr;
		this.path = path;
		this.database = database;
		this.tables = tables;
		this.constraints = constraints;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public String getPackageStr() {
		return packageStr;
	}

	public String getPath() {
		return path;
	}

	public String getDatabase() {
		return database;
	}

	public HashMap<String, Table> getTables() {
		return tables;
	}

	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
