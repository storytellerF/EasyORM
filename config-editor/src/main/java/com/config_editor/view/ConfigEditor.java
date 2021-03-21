package com.config_editor.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.config_editor.model.Config;
import com.config_editor.model.Configs;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.util.Iterator;

public class ConfigEditor {
    private JComboBox<String> config_editor_comboBox1;
    private JPanel panel1;
    private JComboBox<String> config_editor_menu_comboBox1;
    private String path;
    private Gson gson;
    private Configs configs;

    public ConfigEditor() {
    }

    public void init(String suffix, RuntimeTypeAdapterFactory<Config> runtimeTypeAdapterFactory) throws IOException {
        gson=new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
        path = "config-editor-" + suffix + ".json";
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            if (!file.createNewFile()) {
                System.out.println("文件创建失败" + file.getAbsolutePath());
                return;
            }
        }
        InputStream resourceAsStream = new FileInputStream(path);
        configs = gson.fromJson(new InputStreamReader(resourceAsStream), Configs.class);

        resourceAsStream.close();
        config_editor_comboBox1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectedIndex = config_editor_comboBox1.getSelectedIndex();
                Config configAt = configs.getConfigAt(selectedIndex);
                configs.choose(configAt.getId());
                if (listener != null) {
                    listener.onInit(configAt);
                }
            }
        });
        config_editor_menu_comboBox1.addActionListener(e -> {
            Object source = e.getSource();
            if (source instanceof JComboBox) {
                int selectedIndex = config_editor_menu_comboBox1.getSelectedIndex();
                String itemAt = config_editor_menu_comboBox1.getItemAt(selectedIndex);
                System.out.println(itemAt);
                if (itemAt.equals("new")) {
                    if (listener != null) {
                        configs.addConfig(listener.onNew());
                        int index = configs.size() - 1;
                        configs.choose(configs.getConfigAt(index).getId());
                        updateList(index);
                    }
                }
                int selectedIndex1 = config_editor_comboBox1.getSelectedIndex();
                if (selectedIndex1 == -1) return;
                Config configAt = configs.getConfigAt(selectedIndex1);
                switch (itemAt) {
                    case "clone":
                        try {
                            Config clone = (Config) configAt.clone();
                            clone.setName(clone.getName() + "克隆");
                            configs.choose(configs.addConfig(clone));
                            updateList(configs.getLastIndex());
                        } catch (CloneNotSupportedException cloneNotSupportedException) {
                            cloneNotSupportedException.printStackTrace();
                        }
                        break;
                    case "delete":
                        configs.removeAt(selectedIndex1);
                        configs.choose(configs.getConfigAt(0).getId());
                        updateList(0);
                        break;
                    case "rename":
                        String s = JOptionPane.showInputDialog(panel1, "不得与原名称相同" + configAt.getName()
                                , configAt.getName());
                        if (s != null) {
                            configAt.setName(s);
                            updateList(selectedIndex1);
                        }
                        break;
                }
            }
        });
        if (configs == null) {
            System.out.println("configs is null");
            configs = new Configs();
        } else {
            updateList(configs.getLastIndex());
        }
    }

    public void save() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path);
            fileWriter.write(gson.toJson(configs));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateList(int index) {
        config_editor_comboBox1.removeAllItems();
        for (Iterator<Config> it = configs.getIterator(); it.hasNext(); ) {
            Config config = it.next();
            config_editor_comboBox1.addItem(config.getName());
        }
        config_editor_comboBox1.setSelectedIndex(index);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Listener listener;

    public Config getCurrent() {
        return configs.getLastConfig();
    }

    public interface Listener {
        void onInit(Config configs);

        Config onNew();
    }
}
