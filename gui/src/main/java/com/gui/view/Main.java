package com.gui.view;

import com.storyteller_f.easyorm_jdbc.JDBCObtain;
import com.storyteller_f.sql_query.annotation.NoQuery;
import com.config_editor.model.Config;
import com.config_editor.view.ConfigEditor;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.gui.model.MainViewDatabaseConnectionConfig;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.gui.main.*;
import com.gui.model.Constraint;
import com.gui.model.Table;
import com.storyteller_f.sql_query.query.Create;
import com.storyteller_f.sql_query.util.Util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    protected String modelPathFieldString;
    private JButton reflectToDatabase;
    private JButton reflectToCode;
    private JButton produceComponent;
    private JTextField modelPathInput;
    private JButton selectPath;
    private JButton testConnection;
    private JButton reviewAllModel;
    private JPanel contentPanel;
    private JTextField packageNameInput;
    private JTextField linkInput;
    private JTextField portInput;
    private JTextField databaseInput;
    private JTextField nameInput;
    private JPasswordField passwordInput;
    private JTextField urlInput;
    private JButton start;
    private JTextField staticModelPathInput;
    private JButton selectStaticFieldPath;
    private JButton produceStaticFieldModel;
    private JTextField staticModelPathPackageNameInput;
    private JButton readExcel;
    private JTextField extraParamInput;
    private JPanel inputComponent;
    private ConfigEditor configEditor;
    private JButton saveButton;
    private JCheckBox enableLombok;
    private Connection connection;
    private String staticModelPathField;

    public Main() {
        initEditor();
        produceComponent.setVisible(false);
        TextChange textChange = new TextChange();
        loopComponent(textChange);
        //region button event
        selectPath.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setSelectedFile(new File("E:\\Projects\\IdeaProjects\\EasyORM\\gui\\src\\test\\java\\t"));
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int chooserResult = jFileChooser.showOpenDialog(contentPanel);
            switch (chooserResult) {
                case JFileChooser.CANCEL_OPTION:
                case JFileChooser.ERROR_OPTION:
                    break;
                case JFileChooser.APPROVE_OPTION:
                    if (e.getSource().equals(selectPath)) {
                        modelPathFieldString = jFileChooser.getSelectedFile().getAbsolutePath();
                        modelPathInput.setText(modelPathFieldString);
                        packageNameInput.setText(getModelPath(modelPathFieldString));
                    } else {
                        staticModelPathField = jFileChooser.getSelectedFile().getAbsolutePath();
                        staticModelPathInput.setText(staticModelPathField);
                        staticModelPathPackageNameInput.setText(getModelPath(staticModelPathField));
                    }
                    bindPath();
                    break;
            }
        });
        saveButton.addActionListener(e -> configEditor.save());
        selectStaticFieldPath.addActionListener(selectPath.getActionListeners()[0]);
        reviewAllModel.addActionListener(e -> {
            if (modelPathFieldString != null && notEmpty(packageNameInput.getText())) {
                ShowAllModelClass showAllModelClass = new ShowAllModelClass();
                showAllModelClass.showModel(modelPathFieldString, packageNameInput.getText());
                showAllModelClass.show();
            }
        });

        testConnection.addActionListener(e -> {
            boolean b = Util.testConnection(urlInput.getText(), nameInput.getText(), String.valueOf(passwordInput.getPassword()));
            if (b) {
                testConnection.setBackground(Color.green);
            } else {
                testConnection.setBackground(Color.red);
            }
            JOptionPane.showMessageDialog(contentPanel, "连接结果" + b);
        });
        linkInput.addActionListener(e -> urlInput.setText("jdbc:mysql://" + linkInput.getText() + ":" + portInput.getText() + "/" + databaseInput.getText()
                + "?serverTimezone=UTC"));

        reflectToCode.addActionListener(e -> {
            if (checkDatabaseConnection()) return;
            String packageString = packageNameInput.getText();
            String pathString = modelPathInput.getText();
            String databaseString = databaseInput.getText();
            if (!notEmpty(packageString) || !notEmpty(pathString) || !notEmpty(databaseString)) {
                System.out.println("数据输入未完成");
                return;
            }

            try {
                Statement statement = connection.createStatement();
                HashMap<String, Table> tables_hashMap = Util.getTables(statement, databaseInput.getText(), connection);
                ArrayList<Constraint> constraints = Util.getConstraint(databaseInput.getText(), statement);
                Util.addConstraintColumn(tables_hashMap, constraints);
                CreateConfig config = new CreateConfig(packageString, pathString, databaseString, tables_hashMap,
                        constraints, urlInput.getText(), nameInput.getText(),
                        String.valueOf(passwordInput.getPassword()));
                ParseDatabase create = new ParseDatabase(config);
                create.parseDatabase(enableLombok.isSelected());
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(contentPanel, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        start.addActionListener(e -> {
            try {
                connection = DriverManager.getConnection(urlInput.getText(), nameInput.getText(), String.valueOf(passwordInput.getPassword()));
                start.setBackground(Color.GREEN);
            } catch (SQLException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(contentPanel, exception.getMessage());
            }
        });

        readExcel.addActionListener(e -> {
            ReadExcelGetModel readExcelGetModel = new ReadExcelGetModel();
            readExcelGetModel.show();
        });
        reflectToDatabase.addActionListener(e -> {
            if (checkDatabaseConnection()) return;
            if (modelPathFieldString == null || !notEmpty(packageNameInput.getText())) {
                JOptionPane.showMessageDialog(contentPanel, "未配置路径");
                return;
            }
            boolean return_result = ClassLoaderManager.getInstance().oneStep(modelPathFieldString, packageNameInput.getText());
            if (!return_result) {
                int re = JOptionPane.showConfirmDialog(contentPanel, "编译失败，是否清除缓存（清楚之后可以再次进行编译）", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (re == JOptionPane.YES_OPTION) {
                    ClassLoaderManager.getInstance().init();
                }
                return;
            }
            try {
                File file = new File(modelPathFieldString);
                File[] files = file.listFiles();
                if (files == null) {
                    return;
                }
                for (File child : files) {
                    if (child.isDirectory()) {
                        continue;
                    }
                    String className = child.getName().substring(0, child.getName().indexOf('.'));
                    Class<?> clazz = Class.forName(packageNameInput.getText() + "." + className);
                    Create create = new Create(new JDBCObtain(connection), clazz);
                    String parse = create.parse(true);
                    System.out.println(parse);
                    //com.storyteller_f.easyorm_jdbc.connection.createStatement().execute(parse);
                }
                System.out.println("执行完毕");

            } catch (ClassNotFoundException | UnexpectedException e1) {
                e1.printStackTrace();
            }
        });
        //生成静态字段到原文件
        produceStaticFieldModel.addActionListener(e -> {
            if (modelPathFieldString != null && notEmpty(packageNameInput.getText().trim())) {
                ClassLoaderManager.getInstance().oneStep(modelPathFieldString, packageNameInput.getText());
                File[] files = new File(modelPathFieldString).listFiles();
                if (files == null) {
                    return;
                }

                for (File file : files) {
                    StringBuilder fileContent = new StringBuilder();
                    String className = file.getName().substring(0, file.getName().indexOf("."));
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                        String temp;
                        while ((temp = bufferedReader.readLine()) != null) {
                            fileContent.append(temp).append("\n");
                        }
                        if (fileContent.length() == 0) {
                            System.out.println("文件内容为空");
                            continue;
                        }
                        int i;
                        for (i = fileContent.length() - 1; i >= 0; i--) {
                            char c = fileContent.charAt(i);
                            if (c == '}') {
                                //找到文件结尾了
                                break;
                            }
                        }
                        if (i < 0) {
                            //未找到
                            System.out.println("未找到文件尾");
                            continue;
                        }

                        Class<?> clazz = Class.forName(packageNameInput.getText() + "." + className);
                        StringBuilder staticContent = new StringBuilder();
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields) {
                            if (Modifier.isStatic(field.getModifiers())) {
                                continue;
                            }
                            if (field.isAnnotationPresent(NoQuery.class)) {
                                continue;
                            }
                            String name = field.getName();
//                            try {
//                                clazz.getDeclaredField("column_" + name);
//                            } catch (NoSuchFieldException noSuchFieldException) {
//                                staticContent.append("\t@NoQuery\n").append("\tpublic static String s_c_").append(name).append("=\"").append(name).append("\";\n");
//                            }
                            staticContent.append("\tpublic static String ").append(name).append("(){\n").append("\t\treturn ").append(name).append("\n\t}\n");
                        }
                        System.out.println(i + " length:" + fileContent.length());
                        fileContent.insert(i, staticContent);
                        System.out.println("新的文件内容");
                        System.out.println(fileContent.toString());
                        System.out.println("文件内容结束");
                        //todo 替换原文件
//                        String absolutePath = new File(staticModelPathField, className + ".java").getAbsolutePath();
//                        System.out.println(className + " " + absolutePath);
//                        writeFile(absolutePath, fileContent.toString());
                    } catch (ClassNotFoundException | IOException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                }
            }
        });
        //endregion
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Main main = new Main();
        JFrame jFrame = new JFrame("ORM tool");
        ImageIcon imageIcon = new ImageIcon(new File("gui/src/com.gui.main/resources/项目.png").getAbsolutePath());
        jFrame.setIconImage(imageIcon.getImage());
        jFrame.setContentPane(main.contentPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    if (main.connection != null && !main.connection.isClosed()) {
                        main.connection.close();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jFrame.setVisible(true);
    }

    public String getModelPath(String path) {
        String src = "src\\";
        int index = path.lastIndexOf(src);
        String replace = path.substring(index + src.length()).replace("\\", ".");
        if (replace.startsWith("test.java")) {
            return replace.substring("test.java".length() + 1);
        }
        return replace;
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
        staticModelPathPackageNameInput.setText(config.getStaticModelPathPackageName());
        staticModelPathInput.setText(config.getStaticModelPath());
        modelPathFieldString = config.getModelPath();
        staticModelPathField = config.getStaticModelPath();
        enableLombok.setSelected(config.isEnableLombok());
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

    public void initEditor() {
        configEditor.setListener(new ConfigEditor.Listener() {
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
            configEditor.init("com.gui.main", runtimeTypeAdapterFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void bindPath() {
        Config current = configEditor.getCurrent();
        if (current instanceof MainViewDatabaseConnectionConfig) {
            MainViewDatabaseConnectionConfig current1 = (MainViewDatabaseConnectionConfig) current;
            current1.setStaticModelPath(staticModelPathInput.getText());
            current1.setModelPathPackageName(packageNameInput.getText());
            current1.setStaticModelPathPackageName(staticModelPathPackageNameInput.getText());
            current1.setModelPath(modelPathInput.getText());
        }
    }

    private boolean notEmpty(String trim) {
        return trim.length() > 0;
    }

    private boolean checkDatabaseConnection() {
        if (connection == null) {
            JOptionPane.showMessageDialog(contentPanel, "未连接数据库");
            return true;
        }
        return false;
    }

    private void writeFile(String path, String string) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            if (file.isFile()) {
                if (!file.createNewFile()) {
                    System.out.println("createNewFile failure");
                }
            } else {
                if (file.isDirectory()) {
                    if (!file.mkdirs()) {
                        System.out.println("mkdirs failure");
                    }
                } else {
                    if (!file.createNewFile()) {
                        System.out.println("createNewFile failure");
                    }
                }
            }

        }
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(string);
        fileWriter.flush();
        fileWriter.close();
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
