package com.storyteller.gui.main;

import com.storyteller.gui.model.ConnectionConfig;
import com.storyteller.gui.model.Constraint;
import com.storyteller.gui.model.Table;
import com.storyteller.util.Util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class TableConfig {
    private final ConnectionConfig config;
    private final HashMap<String, Table> tables;
    /**
     * 存储表之间的约束信息
     */
    private final ArrayList<Constraint> constraints;

    public TableConfig(ConnectionConfig config, HashMap<String, Table> tables_hashMap, ArrayList<Constraint> constraints) {
        super();
        this.config = config;
        this.tables = tables_hashMap;
        this.constraints = constraints;
    }

    public static TableConfig build(Statement statement, ConnectionConfig config, Connection connection) throws Exception {
        HashMap<String, Table> tables_hashMap = Util.getTables(statement, config.getDatabase(), connection);
        ArrayList<Constraint> constraints = Util.getConstraint(config.getDatabase(), statement);
//        Util.addConstraintColumn(tables_hashMap, constraints);
        return new TableConfig(config, tables_hashMap,constraints);
    }

    public ConnectionConfig getConfig() {
        return config;
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }
}
