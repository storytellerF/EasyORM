package com.config_editor;

import com.config_editor.model.Config;

public interface ConfigEditorReusable {
    void comboBoxBindEvent();
    void save();
    void updateComboBox(int index);
    Config getCurrent();
}
