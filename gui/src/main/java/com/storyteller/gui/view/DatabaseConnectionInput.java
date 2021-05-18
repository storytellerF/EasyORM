package com.storyteller.gui.view;

import com.config_editor.model.Config;
import com.config_editor.view.ConfigEditor;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.storyteller.gui.main.ConnectionConfig;
import com.storyteller.gui.model.MainViewDatabaseConnectionConfig;
import com.storyteller.util.Util;
import com.storyteller_f.uiscale.DataZone;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DatabaseConnectionInput {
    private ConfigEditor configEditor;
    private JPanel inputComponent;
    private JTextField modelPathInput;
    private JButton selectPath;
    private JTextField linkInput;
    private JTextField portInput;
    private JTextField databaseInput;
    private JTextField nameInput;
    private JPasswordField passwordInput;
    private JTextField urlInput;
    private JButton testConnection;
    private JTextField packageNameInput;
    private JTextField extraParamInput;
    private JCheckBox enableLombok;
    @SuppressWarnings({"unused"})
    private JPanel inputGroup;

    public DatabaseConnectionInput() {
        ui();
        initEditor();
        TextChange textChange = new TextChange();
        loopComponent(textChange);
        //region button event
        selectPath.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setSelectedFile(new File("E:\\Projects\\IdeaProjects\\EasyORM\\gui\\src\\test\\java\\t"));
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int chooserResult = jFileChooser.showOpenDialog(linkInput.getParent());
            switch (chooserResult) {
                case JFileChooser.CANCEL_OPTION:
                case JFileChooser.ERROR_OPTION:
                    break;
                case JFileChooser.APPROVE_OPTION:
                    if (e.getSource().equals(selectPath)) {
                        String modelPathFieldString;
                        modelPathFieldString = jFileChooser.getSelectedFile().getAbsolutePath();
                        modelPathInput.setText(modelPathFieldString);
                        packageNameInput.setText(getModelPathFromPath(modelPathFieldString));
                    }
                    bindPath();
                    break;
            }
        });
        testConnection.addActionListener(e -> {
            boolean b = Util.testConnection(urlInput.getText(), nameInput.getText(), String.valueOf(passwordInput.getPassword()));
            if (b) {
                testConnection.setBackground(Color.green);
            } else {
                testConnection.setBackground(Color.red);
            }
            JOptionPane.showMessageDialog(linkInput.getParent(), "连接结果" + b);
        });
    }

    private void ui() {
//        DataZone.setFont();
    }

    public void initEditor() {
        configEditor.setListener(new ConfigEditor.ConfigEditorListener() {
            @Override
            public void onInit(Config config) {
                if (config instanceof MainViewDatabaseConnectionConfig) {
                    MainViewDatabaseConnectionConfig config1 = (MainViewDatabaseConnectionConfig) config;
                    bind(config1);
                }
            }

            @Override
            public Config onNew() {
                MainViewDatabaseConnectionConfig loginConfig =
                        new MainViewDatabaseConnectionConfig();
                loginConfig.setName("未命名" + System.currentTimeMillis());
                return loginConfig;
            }
        });
        RuntimeTypeAdapterFactory<Config> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Config.class)
                .registerSubtype(MainViewDatabaseConnectionConfig.class);
        try {
            configEditor.init("com.storyteller.gui.main.DatabaseConnectionInput", runtimeTypeAdapterFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将对象中的数据填充到控件上
     *
     * @param config
     */
    public void bind(MainViewDatabaseConnectionConfig config) {
        linkInput.setText(config.getLink());
        urlInput.setText(config.getUrl());
        portInput.setText(config.getPort());
        modelPathInput.setText(config.getModelPath());
        packageNameInput.setText(config.getModelPathPackageName());
        databaseInput.setText(config.getDatabase());
        extraParamInput.setText(config.getExtraParams());
        nameInput.setText(config.getUserName());
        passwordInput.setText(config.getPassword());
//        staticModelPathPackageNameInput.setText(config.getStaticModelPathPackageName());
//        staticModelPathInput.setText(config.getStaticModelPath());
//        modelPathFieldString = config.getModelPath();
//        staticModelPathField = config.getStaticModelPath();
        enableLombok.setSelected(config.isEnableLombok());
    }

    public String getModelPathFromPath(String path) {
        String src = "src\\";
        int index = path.lastIndexOf(src);
        String replace = path.substring(index + src.length()).replace("\\", ".");
        if (replace.startsWith("test.java")) {
            return replace.substring("test.java".length() + 1);
        }
        return replace;
    }

    public void bindPath() {
        Config current = configEditor.getCurrent();
        if (current instanceof MainViewDatabaseConnectionConfig) {
            MainViewDatabaseConnectionConfig current1 = (MainViewDatabaseConnectionConfig) current;
//            current1.setStaticModelPath(staticModelPathInput.getText());
            current1.setModelPathPackageName(packageNameInput.getText());
//            current1.setStaticModelPathPackageName(staticModelPathPackageNameInput.getText());
            current1.setModelPath(modelPathInput.getText());
        }
    }

    /**
     * 将数据保存到对象中
     */
    public void dnib() {
        Config configEditorCurrent = configEditor.getCurrent();
        if (configEditorCurrent instanceof MainViewDatabaseConnectionConfig) {
            MainViewDatabaseConnectionConfig current = (MainViewDatabaseConnectionConfig) configEditorCurrent;
            current.setLink(linkInput.getText());
            current.setUrl(urlInput.getText());
            current.setDatabase(databaseInput.getText());
            current.setExtraParams(extraParamInput.getText());
            current.setUserName(nameInput.getText());
            current.setPassword(String.valueOf(passwordInput.getPassword()));
            current.setPort(portInput.getText());
            current.setEnableLombok(enableLombok.isSelected());
        }
    }

    private void loopComponent(TextChange textChange) {
        Component[] components = inputComponent.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField jTextField = (JTextField) component;
                String name = jTextField.getName();
                if (name != null && name.equals("database")) {
                    ((JTextField) component).getDocument().addDocumentListener(textChange);
                }
            }
        }
    }

    public void saveConfig() {
        configEditor.save();
    }

    public boolean checkModel() {
        return (modelPathInput.getText() != null && notEmpty(packageNameInput.getText()));
    }

    public boolean checkAll() {
//        if (checkDatabaseConnection()) return false;
        String packageString = packageNameInput.getText();
        String pathString = modelPathInput.getText();
        String urlInputText = urlInput.getText();
        if (!notEmpty(packageString) || !notEmpty(pathString) || !notEmpty(urlInputText)) {
            System.out.println("数据输入未完成");
            return false;
        }
        return true;
    }

    private boolean notEmpty(String trim) {
        return trim.length() > 0;
    }

    public String packageName() {
        return packageNameInput.getText();
    }

    public String getDatabaseName() {
        return databaseInput.getText();
    }

    public ConnectionConfig getCreateConfig() {
        return new ConnectionConfig(packageNameInput.getText(), modelPathInput.getText(), databaseInput.getText(), urlInput.getText(), nameInput.getText(), String.valueOf(passwordInput.getPassword()));
    }

    public boolean isEnableLomok() {
        return enableLombok.isSelected();
    }

    public String getModel() {
        return modelPathInput.getText();
    }


    class TextChange implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            getaVoid();
        }

        private void getaVoid() {
            urlInput.setText(String.format("jdbc:mysql://%s:%s/%s?%s", linkInput.getText(), portInput.getText(), databaseInput.getText(), extraParamInput.getText()));
            dnib();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            getaVoid();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
}
