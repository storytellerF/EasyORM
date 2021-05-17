package com.storyteller.gui.main;

import java.io.File;

public class FileState {
    /**
     * 用于标识文件状态，进而确认是否需要将生成的内容生成到html文件中
     * 状态：0 未打开文件，不可以进行
     *      1 文件存在，可以进行
     *      2 文件不存在，不可以进行
     */
    private int state=0;

    public String getPath() {
        return path;
    }

    private String path;

    public void open(String path) {
        open(new File(path));
    }

    public void open(File file) {
        this.path=file.getPath();
        if (file.exists()) {
            state = 1;
        } else {
            state=2;
        }
        listener.state(accept());
    }

    public boolean accept() {
        return state == 1;
    }
    Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener{
        /**
         *
         * @param state true：可以进行 false 不可以进行
         */
        void state(boolean state);
    }
}
