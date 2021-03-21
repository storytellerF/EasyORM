package com.config_editor.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configs {
    private final ArrayList<Config> configs;
    private int last;
    private int max;

    public Configs() {
        configs = new ArrayList<>();
    }

    public ArrayList<Config> getConfigs() {
        return configs;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * 上一次的索引位置
     *
     * @return 如果找不到返回-1
     */
    public int getLastIndex() {
        return getIndex(last);
    }

    public int getLast() {
        return this.last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public Iterator<Config> getIterator() {
        return configs.iterator();
    }

    /**
     * 会自动设置id
     *
     * @param config 对象
     * @return 返回生成的id
     */
    public int addConfig(Config config) {
        config.setId(max);
        max++;
        configs.add(config);
        return config.getId();
    }

    /**
     * 获取正在选中的项
     *
     * @return 正在选中的项
     */
    public Config getLastConfig() {
        return this.getConfig(this.getLast());
    }

    public Config getConfigAt(int index) {
        return this.configs.get(index);
    }

    public int getIndex(int id) {
        int index = 0;
        for (int i = 0; i < configs.size(); i++) {
            Config config = configs.get(i);
            if (config.getId() == id) {
                return index;
            }

            ++index;
        }
        return -1;
    }

    public Config getConfig(int id) {
        for (Config config : configs) {
            if (config.getId() == id) {
                return config;
            }
        }
        return null;
    }

    public void choose(int id) {
        this.last = id;
    }

    public void removeAt(int selectedIndex) {
        configs.remove(selectedIndex);
    }

    public int size() {
        return configs.size();
    }

    public void addAll(List<Config> configList) {
        configs.addAll(configList);
    }
}
