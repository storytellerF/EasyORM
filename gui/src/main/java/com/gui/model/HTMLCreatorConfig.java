package com.gui.model;

import com.storyteller_f.sql_query.annotation.type.string.EnumColumn;
import com.config_editor.model.Config;

public class HTMLCreatorConfig extends Config {
    @EnumColumn({"bootstrap", "regular"})
    private String type;
    private int verify;
    private String path;
    @EnumColumn({"utf-8", "gbk", "gb2312"})
    private String charset;

    @Override
    public String toString() {
        return "HTMLCreatorConfig{" +
                "type='" + type + '\'' +
                ", verify=" + verify +
                ", path='" + path + '\'' +
                ", charset='" + charset + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        HTMLCreatorConfig HTMLCreatorConfig = new HTMLCreatorConfig();
        HTMLCreatorConfig.setName(getName());
        HTMLCreatorConfig.setPath(path);
        HTMLCreatorConfig.setVerify(verify);
        HTMLCreatorConfig.setCharset(charset);
        HTMLCreatorConfig.setType(type);

        return HTMLCreatorConfig;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}