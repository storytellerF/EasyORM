package com.storyteller.gui.view;

import com.config_editor.model.Config;
import com.config_editor.view.ConfigEditorUI;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.storyteller.gui.model.ConnectionConfig;
import com.storyteller.gui.model.MainViewDatabaseConnectionConfig;
import com.storyteller.util.Util;
import com.storyteller_f.relay_message.RelayMessage;
import com.storyteller_f.uiscale.UIUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DatabaseConnectionInput {
    private ConfigEditorUI configEditorUI;
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
    private boolean binding=false;
    @SuppressWarnings({"unused"})
    private JPanel inputGroup;

    public DatabaseConnectionInput() {
        UIUtil.setFont(testConnection,enableLombok);
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
                        savePath();
                    }
                    break;
            }
        });
        testConnection.addActionListener(e -> {
            RelayMessage b = Util.testConnection(urlInput.getText(), nameInput.getText(), String.valueOf(passwordInput.getPassword()));
            if (b.isSuccess) {
                testConnection.setBackground(Color.green);
            } else {
                testConnection.setBackground(Color.red);
                System.out.println(b.message);
            }
            JOptionPane.showMessageDialog(linkInput.getParent(), "????????????" + b.isSuccess);
        });
    }

    public void initEditor() {
        configEditorUI.setListener(new ConfigEditorUI.ConfigEditorListener() {
            @Override
            public void onShow(Config config) {
                if (config instanceof MainViewDatabaseConnectionConfig) {
                    binding=true;
                    MainViewDatabaseConnectionConfig config1 = (MainViewDatabaseConnectionConfig) config;
                    linkInput.setText(config1.getLink());
                    urlInput.setText(config1.getUrl());
                    portInput.setText(config1.getPort());
                    modelPathInput.setText(config1.getModelPath());
                    packageNameInput.setText(config1.getModelPathPackageName());
                    databaseInput.setText(config1.getDatabase());
                    extraParamInput.setText(config1.getExtraParams());
                    nameInput.setText(config1.getUserName());
                    passwordInput.setText(config1.getPassword());
                    enableLombok.setSelected(config1.isEnableLombok());
                    urlInput.setText(String.format("jdbc:mysql://%s:%s/%s?%s", linkInput.getText(), portInput.getText(), databaseInput.getText(), extraParamInput.getText()));
                    binding=false;
                }
            }

            @Override
            public Config onNew() {
                MainViewDatabaseConnectionConfig connectionConfig = new MainViewDatabaseConnectionConfig();
                connectionConfig.setName("?????????" + System.currentTimeMillis());
                return connectionConfig;
            }

            @Override
            public void onUiChange(Config configEditorCurrent) {
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
                    current.setModelPath(modelPathInput.getText());
                    current.setModelPathPackageName(packageNameInput.getText());
                }
            }
        });
        RuntimeTypeAdapterFactory<Config> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Config.class)
                .registerSubtype(MainViewDatabaseConnectionConfig.class);
        try {
            configEditorUI.init(this.getClass().getName(), runtimeTypeAdapterFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void savePath() {
        Config current = configEditorUI.getCurrent();
        if (current instanceof MainViewDatabaseConnectionConfig) {
            MainViewDatabaseConnectionConfig current1 = (MainViewDatabaseConnectionConfig) current;
            current1.setModelPathPackageName(packageNameInput.getText());
            current1.setModelPath(modelPathInput.getText());
        }
    }

    private void loopComponent(TextChange textChange) {
        Component[] components = inputComponent.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField jTextField = (JTextField) component;
                String name = jTextField.getName();

                if (name != null &&(name.equals("database")||name.equals("model"))) {
                    ((JTextField) component).getDocument().addDocumentListener(textChange);
                }
            }
        }
    }

    public void saveConfig() {
        configEditorUI.save();
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
            System.out.println("?????????????????????");
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
            textChange();
        }

        private void textChange() {
            if (binding) return;
            urlInput.setText(String.format("jdbc:mysql://%s:%s/%s?%s", linkInput.getText(), portInput.getText(), databaseInput.getText(), extraParamInput.getText()));
            configEditorUI.saveChange();
//            bindPath();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            textChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
}
