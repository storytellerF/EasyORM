package com.config_editor.view;

import com.config_editor.model.Config;
import com.config_editor.model.Configs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.*;
import java.util.Iterator;

public class ConfigEditorCore {
    private String path;
    private Gson gson;
    private Configs configs;
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
    public void init(String suffix, TypeAdapterFactory... runtimeTypeAdapterFactory) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        for (TypeAdapterFactory configRuntimeTypeAdapterFactory : runtimeTypeAdapterFactory) {
            gsonBuilder.registerTypeAdapterFactory(configRuntimeTypeAdapterFactory);
        }
        gson= gsonBuilder.create();
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
        coreListener.bindEvent();
        after();
    }

    public void after() {
        if (configs == null) {
            System.out.println("configs is null");
            configs = new Configs();
        } else {
            if (coreListener != null) {
                coreListener.updateList(configs.getLastIndex());
            }
        }
    }

    public void setCoreListener(CoreListener coreListener) {
        this.coreListener = coreListener;
    }

    CoreListener coreListener;

    public void choose(int id) {
        configs.choose(id);
    }

    public int addConfig(Config onNew) {
       return configs.addConfig(onNew);
    }

    public int size() {
        return configs.size();
    }

    public Config getConfigAt(int index) {
        return configs.getConfigAt(index);
    }

    public int getLastIndex() {
        return configs.getLastIndex();
    }

    public void removeAt(int selectedIndex1) {
        configs.removeAt(selectedIndex1);
    }

    public Iterator<Config> getIterator() {
        return configs.getIterator();
    }

    public Config getLastConfig() {
        return configs.getLastConfig();
    }

    public void chooseAt(int selectedIndex) {
        Config configAt = configs.getConfigAt(selectedIndex);
        configs.choose(configAt.getId());
    }

    public void chooseTail() {
        int index = configs.size() - 1;
        chooseAt(index);
    }

    public void sendCommand(String command, int selectedList) {
        if (command.equals("new")) {
            if (coreListener != null) {
                addConfig(coreListener.onNew());
                int index = size() - 1;
                chooseAt(index);
                coreListener.updateList(index);
                return;
            }
        }
        if (selectedList == -1) return;
        Config configAt = getConfigAt(selectedList);
        switch (command) {
            case "clone":
                try {
                    Config clone = (Config) configAt.clone();
                    clone.setName(clone.getName() + "克隆");
                    choose(addConfig(clone));
                    coreListener.updateList(getLastIndex());
                } catch (CloneNotSupportedException cloneNotSupportedException) {
                    cloneNotSupportedException.printStackTrace();
                }
                break;
            case "delete":
                removeAt(selectedList);
                choose(getConfigAt(0).getId());
                coreListener.updateList(0);
                break;
        }
    }

    public void selected(int selectedIndex) {
        Config configAt = getConfigAt(selectedIndex);
        choose(configAt.getId());
        if (coreListener != null) {
            coreListener.onInit(configAt);
        }
    }

    interface CoreListener{
        /**
         * 更新选项，传入的参数，代表应该选中的项
         * @param config 选中的索引
         */
        void updateList(int config);

        Config onNew();

        void onInit(Config configAt);

        void bindEvent();
    }
}
