package com.config_editor.model;

public class Config implements Cloneable {
    private int id;
    private String name;

    public Config(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Config() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
