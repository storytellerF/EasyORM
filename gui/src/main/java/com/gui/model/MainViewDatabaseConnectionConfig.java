package com.gui.model;

public class MainViewDatabaseConnectionConfig extends com.config_editor.model.Config {
    private String link;
    private String port;
    private String database;
    private String userName;
    private String password;
    private String extraParams;
    private String url;
    private String modelPath;
    private String modelPathPackageName;
    private String staticModelPath;
    private String staticModelPathPackageName;

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

    public String getStaticModelPath() {
        return staticModelPath;
    }

    public void setStaticModelPath(String staticModelPath) {
        this.staticModelPath = staticModelPath;
    }

    public String getStaticModelPathPackageName() {
        return staticModelPathPackageName;
    }

    public void setStaticModelPathPackageName(String staticModelPathPackageName) {
        this.staticModelPathPackageName = staticModelPathPackageName;
    }
}