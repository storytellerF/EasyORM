package com.storyteller.gui.model;

import com.config_editor.model.Config;

public class MainViewDatabaseConnectionConfig extends Config {
    private String link;
    private String port;
    private String database;
    private String userName;
    private String password;
    private String extraParams;
    private String url;
    private String modelPath;
    private String modelPathPackageName;
    private boolean enableLombok;

    public boolean isEnableLombok() {
        return enableLombok;
    }

    public void setEnableLombok(boolean enableLombok) {
        this.enableLombok = enableLombok;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(String extraParams) {
        this.extraParams = extraParams;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getModelPathPackageName() {
        return modelPathPackageName;
    }

    public void setModelPathPackageName(String modelPathPackageName) {
        this.modelPathPackageName = modelPathPackageName;
    }
}
