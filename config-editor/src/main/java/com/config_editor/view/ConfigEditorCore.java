package com.config_editor.view;

import com.config_editor.ConfigReusable;
import com.config_editor.model.Config;
import com.config_editor.model.Configs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import java.io.*;
import java.util.Iterator;

public class ConfigEditorCore implements ConfigReusable {
    CoreListener coreListener;
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
        gson = gsonBuilder.create();
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

    public void removeAt(int index) {
        configs.removeAt(index);
    }

    public Iterator<Config> getIterator() {
        return configs.getIterator();
    }

    public Config getLastConfig() {
        return configs.getLastConfig();
    }

    public void sendCommand(String command, int selectedList) {
        if (command.equals("new")) {
            if (coreListener != null) {
                choose(addConfig(coreListener.onNew()));
                coreListener.updateList(size() - 1);
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

    /**
     * 选中指定位置的配置
     * @param selectedIndex 选中的索引
     */
    public void selected(int selectedIndex) {
        Config config = getConfigAt(selectedIndex);
        choose(config.getId());
        if (coreListener != null) {
            coreListener.onShow(config);
        }
        save();
    }

    interface CoreListener {
        /**
         * 更新选项，传入的参数，代表应该选中的项
         *
         * @param indexOfConfig 选中的索引
         */
        void updateList(int indexOfConfig);

        Config onNew();

        void onShow(Config config);

        void bindEvent();
    }
}
