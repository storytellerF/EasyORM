package com.config_editor.view;

import com.config_editor.ConfigEditorReusable;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.config_editor.model.Config;
import com.storyteller_f.uiscale.DataZone;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.util.Iterator;

public class ConfigEditorUI implements ConfigEditorReusable {
    private JComboBox<String> main_comboBox;
    private JPanel contentPanel;
    private JComboBox<String> menu_comboBox;
    private final ConfigEditorCore core;

    public ConfigEditorUI() {
        DataZone.setFont(main_comboBox, menu_comboBox);
        core=new ConfigEditorCore();
    }

    public void init(String suffix, RuntimeTypeAdapterFactory<Config> runtimeTypeAdapterFactory) throws IOException {
        core.setCoreListener(new ConfigEditorCore.CoreListener() {
            @Override
            public void updateList(int indexOfConfig) {
                updateComboBox(indexOfConfig);//此函数会选中相应的配置，并出发事件，更新内容
            }

            @Override
            public Config onNew() {
                return configEditorListener.onNew();
            }

            @Override
            public void onShow(Config config) {
                configEditorListener.onShow(config);
            }

            @Override
            public void bindEvent() {
                comboBoxBindEvent();
            }
        });
        core.init(suffix, runtimeTypeAdapterFactory);
    }

    public void comboBoxBindEvent() {
        main_comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectedIndex = main_comboBox.getSelectedIndex();
                core.selected(selectedIndex);
            }
        });
        menu_comboBox.addActionListener(e -> {
            Object source = e.getSource();
            if (source instanceof JComboBox) {
                int menuIndex = menu_comboBox.getSelectedIndex();
                String menuCommand = menu_comboBox.getItemAt(menuIndex);
                int selectedListItem = main_comboBox.getSelectedIndex();
                System.out.println(menuCommand);
                if ("rename".equals(menuCommand)) {
                    Config configAt = core.getConfigAt(selectedListItem);
                    String s = JOptionPane.showInputDialog(contentPanel, "不得与原名称相同" + configAt.getName()
                            , configAt.getName());
                    if (s != null) {
                        configAt.setName(s);
                        updateComboBox(selectedListItem);
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

    /**
     * 更新comboBox 的内容，然后选中
     * @param index 应该被选中的索引位置
     */
    public void updateComboBox(int index) {
        main_comboBox.removeAllItems();
        for (Iterator<Config> it = core.getIterator(); it.hasNext(); ) {
            Config config = it.next();
            main_comboBox.addItem(config.getName());
        }
        main_comboBox.setSelectedIndex(index);
    }

    public void setListener(ConfigEditorListener configEditorListener) {
        this.configEditorListener = configEditorListener;
    }

    private ConfigEditorListener configEditorListener;

    public Config getCurrent() {
        return core.getLastConfig();
    }

    public interface ConfigEditorListener {
        void onShow(Config configs);

        Config onNew();
    }

    public static void main(String[] args) {
        JFrame jFrame=new JFrame();
        ConfigEditorUI configEditorUI =new ConfigEditorUI();
        jFrame.setContentPane(configEditorUI.contentPanel);
        jFrame.setVisible(true);
    }
}
