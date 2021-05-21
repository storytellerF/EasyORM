package com.config_editor.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Configs {
    private final ArrayList<Config> configs;
    private int last;
    private int max;

    public Configs() {
        configs = new ArrayList<>();
    }

    /**
     * 上一次的索引位置
     *
     * @return 如果找不到返回-1
     */
    public int getLastIndex() {
        return getIndex(last);
    }

    public Iterator<Config> getIterator() {
        return configs.iterator();
    }

    /**
     * 会自动设置id，id 从0开始
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
        return getConfig(last);
    }

    /**
     * 获取指定索引位置的配置
     * @param index 索引位置
     * @return 配置
     */
    public Config getConfigAt(int index) {
        return configs.get(index);
    }

    /**
     * 通过id 获取索引位置
     * @param id 配置的id
     * @return 如果找不到，返回-1
     */
    public int getIndex(int id) {
        int index = 0;
        for (Config config : configs) {
            if (config.getId() == id) {
                return index;
            }

            ++index;
        }
        return -1;
    }

    /**
     * 获取指定id 的索引
     * @param id id
     * @return 返回找到的配置
     */
    public Config getConfig(int id) {
        int index=getIndex(id);
        if (index != -1) {
            return configs.get(index);
        }
        return null;
    }

    /**
     * 设定当前选择的配置的id
     * @param id 需要被选中的配置的id
     */
    public void choose(int id) {
        this.last = id;
    }

    /**
     * 移除指定位置的配置
     * @param index 索引位置
     */
    public void removeAt(int index) {
        configs.remove(index);
    }

    public int size() {
        return configs.size();
    }

//    public void addAll(List<Config> configList) {
//        configs.addAll(configList);
//    }
}
