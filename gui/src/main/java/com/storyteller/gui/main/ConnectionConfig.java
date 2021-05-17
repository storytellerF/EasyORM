package com.storyteller.gui.main;

public class ConnectionConfig {
    private final String packageStr;
    private final String path;
    private final String database;
    private final String url;
    private final String user;
    private final String password;

    public ConnectionConfig(String packageStr, String path, String database, String url, String user, String password) {
        this.packageStr = packageStr;
        this.path = path;
        this.database = database;
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
