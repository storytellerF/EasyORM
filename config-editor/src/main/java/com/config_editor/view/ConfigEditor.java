package com.config_editor.view;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.config_editor.model.Config;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.util.Iterator;

public class ConfigEditor {
    private JComboBox<String> config_editor_comboBox;
    private JPanel contentPanel;
    private JComboBox<String> config_editor_menu_comboBox;
    private final ConfigEditorCore core;

    public ConfigEditor() {
        core=new ConfigEditorCore();
    }

    public void init(String suffix, RuntimeTypeAdapterFactory<Config> runtimeTypeAdapterFactory) throws IOException {
        core.setCoreListener(new ConfigEditorCore.CoreListener() {
            @Override
            public void updateList(int config) {
                update(config);
            }

            @Override
            public Config onNew() {
                return configEditorListener.onNew();
            }

            @Override
            public void onInit(Config configAt) {
                configEditorListener.onInit(configAt);
            }

            @Override
            public void bindEvent() {
                bindEvent1();
            }
        });
        core.init(suffix, runtimeTypeAdapterFactory);
    }

    private void bindEvent1() {
        config_editor_comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectedIndex = config_editor_comboBox.getSelectedIndex();
                core.selected(selectedIndex);
            }
        });
        config_editor_menu_comboBox.addActionListener(e -> {
            Object source = e.getSource();
            if (source instanceof JComboBox) {
                int menuIndex = config_editor_menu_comboBox.getSelectedIndex();
                String menuCommand = config_editor_menu_comboBox.getItemAt(menuIndex);
                int selectedListItem = config_editor_comboBox.getSelectedIndex();
                System.out.println(menuCommand);
                if ("rename".equals(menuCommand)) {
                    Config configAt = core.getConfigAt(selectedListItem);
                    String s = JOptionPane.showInputDialog(contentPanel, "不得与原名称相同" + configAt.getName()
                            , configAt.getName());
                    if (s != null) {
                        configAt.setName(s);
                        update(selectedListItem);
                    }
                }else {
                    core.sendCommand(menuCommand, selectedListItem);
                }
            }
        });
    }

    public void save() {
        core.save();
    }

    public void update(int index) {
        config_editor_comboBox.removeAllItems();
        for (Iterator<Config> it = core.getIterator(); it.hasNext(); ) {
            Config config = it.next();
            config_editor_comboBox.addItem(config.getName());
        }
        config_editor_comboBox.setSelectedIndex(index);
    }

    public void setListener(ConfigEditorListener configEditorListener) {
        this.configEditorListener = configEditorListener;
    }

    private ConfigEditorListener configEditorListener;

    public Config getCurrent() {
        return core.getLastConfig();
    }

    public interface ConfigEditorListener {
        void onInit(Config configs);

        Config onNew();
    }

    public static void main(String[] args) {
        JFrame jFrame=new JFrame();
        ConfigEditor configEditor=new ConfigEditor();
        jFrame.setContentPane(configEditor.contentPanel);
        jFrame.setVisible(true);
    }
}
