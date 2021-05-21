import com.config_editor.view.ConfigEditorUI;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.config_editor.model.Config;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class TestConfigEditor {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private ConfigEditorUI configEditorUI;
    private JButton saveButton;

    public TestConfigEditor() {
        try {
            configEditorUI.setListener(new ConfigEditorUI.ConfigEditorListener() {
                @Override
                public void onShow(Config config) {
                    if (config instanceof LoginConfig) {
                        LoginConfig config1 = (LoginConfig) config;
                        textField2.setText(config1.getUsername());
                        textField1.setText(config1.getPassword());
                    }
                }

                @Override
                public Config onNew() {
                    LoginConfig loginConfig = new LoginConfig("", "");
                    loginConfig.setName("未命名"+System.currentTimeMillis());
                    return loginConfig;
                }
            });
            RuntimeTypeAdapterFactory<Config> runtimeTypeAdapterFactory=RuntimeTypeAdapterFactory.of(Config.class)
                    .registerSubtype(LoginConfig.class);
            configEditorUI.init("test-config-editor",runtimeTypeAdapterFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveButton.addActionListener(e -> configEditorUI.save());
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                ((LoginConfig) configEditorUI.getCurrent()).setPassword(textField1.getText());

            }
        });
        textField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                ((LoginConfig) configEditorUI.getCurrent()).setUsername(textField2.getText());
            }
        });
    }

    public static void main(String[] args) {
        TestConfigEditor testConfigEditor=new TestConfigEditor();
        JFrame jFrame=new JFrame("测试");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setContentPane(testConfigEditor.panel1);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
