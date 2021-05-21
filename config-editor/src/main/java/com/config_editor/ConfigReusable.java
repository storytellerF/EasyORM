package com.config_editor;

import com.config_editor.model.Config;

public interface ConfigReusable {
    /**
     * 选择指定id 的配置
     *
     * @param id 配置的id ，从0开始
     */
    void choose(int id);

    /**
     * 返回配置的个数
     * @return 配置的个数
     */
    int size();

    /**
     * 获取指定索引的配置
     * @param index 索引，从0开始，如果不存在，会产生异常
     * @return 返回查找到的索引
     */
    Config getConfigAt(int index);

    /**
     * 获取上一次选中的索引
     * @return 上一次选中的配置的索引位置
     */
    int getLastIndex();

    /**
     * 移除指定索引位置的配置
     * @param index 索引位置
     */
    void removeAt(int index);

    /**
     * 上一次选中的配置
     * @return 配置
     */
    Config getLastConfig();

}
