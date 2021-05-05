package com.gui.main;

import com.gui.model.Constraint;
import com.gui.model.Table;
import com.storyteller_f.sql_query.util.Util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateConfig {
    private final ConnectionConfig config;
    private final HashMap<String, Table> tables;
    private final ArrayList<Constraint> constraints;

    public CreateConfig(ConnectionConfig config, HashMap<String, Table> tables_hashMap, ArrayList<Constraint> constraints) {
        super();
        this.config = config;
        this.tables = tables_hashMap;
        this.constraints = constraints;
    }

    public static CreateConfig build(Statement statement, ConnectionConfig config, Connection connection) throws Exception {
        HashMap<String, Table> tables_hashMap = Util.getTables(statement, config.getDatabase(), connection);
        ArrayList<Constraint> constraints = Util.getConstraint(config.getDatabase(), statement);
        Util.addConstraintColumn(tables_hashMap, constraints);
        return new CreateConfig(config, tables_hashMap,constraints);
    }

    public ConnectionConfig getConfig() {
        return config;
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }


}
