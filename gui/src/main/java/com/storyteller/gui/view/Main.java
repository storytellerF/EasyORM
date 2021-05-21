package com.storyteller.gui.view;

import com.storyteller.gui.main.ClassLoaderManager;
import com.storyteller.gui.main.ConnectionConfig;
import com.storyteller.gui.main.CreateConfig;
import com.storyteller.gui.main.ParseDatabase;
import com.storyteller_f.easyorm_jdbc.JDBCObtain;
import com.storyteller_f.sql_query.annotation.NoQuery;
import com.storyteller_f.sql_query.query.Create;
import com.storyteller_f.uiscale.DataZone;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
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
import java.util.List;

public class Main {
    private JButton reflectToDatabase;
    private JButton reflectToCode;
    private JButton produceComponent;
    private JButton reviewAllModel;
    private JPanel contentPanel;
    private JButton start;
    private JButton produceStaticFieldModel;
    private JButton readExcel;
    private JButton saveButton;
    private DatabaseConnectionInput databaseConnectionInput;
    private Connection connection;

    public Main() {
        List<JButton> buttonList = new ArrayList<>();
        buttonList.add(start);
        buttonList.add(produceComponent);
        buttonList.add(readExcel);
        buttonList.add(saveButton);
        buttonList.add(reflectToCode);
        buttonList.add(reflectToDatabase);
        buttonList.add(reviewAllModel);
        buttonList.add(produceStaticFieldModel);
        for (JButton jButton : buttonList) {
            jButton.setFont(new Font("黑体", jButton.getFont().getStyle(), jButton.getFont().getSize()));
        }
        DataZone.setFont(start,produceComponent,readExcel,saveButton,reflectToCode,reflectToDatabase,reviewAllModel,produceStaticFieldModel);
        produceComponent.setVisible(false);
        saveButton.addActionListener(e -> databaseConnectionInput.saveConfig());
        reviewAllModel.addActionListener(e -> {
            if (databaseConnectionInput.checkModel()) {
                ShowAllModelClass showAllModelClass = new ShowAllModelClass();
                showAllModelClass.showModel(databaseConnectionInput.getModel(), databaseConnectionInput.packageName());
                showAllModelClass.show();
            }
        });

        reflectToCode.addActionListener(e -> {
            if (checkDatabaseConnection()) return;
            if (!databaseConnectionInput.checkAll()) return;

            try {
                Statement statement = connection.createStatement();
                ConnectionConfig config = databaseConnectionInput.getCreateConfig();
                CreateConfig createConfig = CreateConfig.build(statement, config, connection);
                ParseDatabase create = new ParseDatabase(createConfig);
                create.parseDatabase(databaseConnectionInput.isEnableLomok());
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(contentPanel, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        start.addActionListener(e -> {
            try {
                ConnectionConfig config = databaseConnectionInput.getCreateConfig();
                connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
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
            if (!databaseConnectionInput.checkModel()) {
                JOptionPane.showMessageDialog(contentPanel, "未配置路径");
                return;
            }
            boolean return_result = ClassLoaderManager.getInstance().oneStep(databaseConnectionInput.getModel(), databaseConnectionInput.packageName());
            if (!return_result) {
                int re = JOptionPane.showConfirmDialog(contentPanel, "编译失败，是否清除缓存（清楚之后可以再次进行编译）", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (re == JOptionPane.YES_OPTION) {
                    ClassLoaderManager.getInstance().init();
                }
                return;
            }
            try {
                File file = new File(databaseConnectionInput.getModel());
                File[] files = file.listFiles();
                if (files == null) {
                    return;
                }
                for (File child : files) {
                    if (child.isDirectory()) {
                        continue;
                    }
                    String className = child.getName().substring(0, child.getName().indexOf('.'));
                    Class<?> clazz = Class.forName(databaseConnectionInput.packageName() + "." + className);
                    Create create = new Create(new JDBCObtain(connection), clazz);
                    String parse = create.parse(true);
                    System.out.println(parse);
                    connection.createStatement().execute(parse);
                }
                System.out.println("执行完毕");

            } catch (ClassNotFoundException | UnexpectedException | SQLException e1) {
                e1.printStackTrace();
            }
        });
        //生成静态字段到原文件
        produceStaticFieldModel.addActionListener(e -> {
            if (!databaseConnectionInput.checkModel()) {
                return;
            }
            boolean b = ClassLoaderManager.getInstance().oneStep(databaseConnectionInput.getModel(), databaseConnectionInput.packageName());
            if (!b) {
                int re = JOptionPane.showConfirmDialog(contentPanel, "编译失败，是否清除缓存（清楚之后可以再次进行编译）", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (re == JOptionPane.YES_OPTION) {
                    ClassLoaderManager.getInstance().init();
                }
                return;
            }
            File[] files = new File(databaseConnectionInput.getModel()).listFiles();
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

                    Class<?> clazz = Class.forName(databaseConnectionInput.packageName() + "." + className);
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
                        staticContent.append("\tpublic static String ").append(name).append("(){\n").append("\t\treturn \"").append(name).append("\";\n\t}\n");
                    }
                    System.out.println(i + " length:" + fileContent.length());
                    fileContent.insert(i, staticContent);
                    System.out.println("新的文件内容");
                    System.out.println(fileContent);
                    System.out.println("文件内容结束");
                    //todo 替换原文件
//                        String absolutePath = new File(staticModelPathField, className + ".java").getAbsolutePath();
//                        System.out.println(className + " " + absolutePath);
//                        writeFile(absolutePath, fileContent.toString());
                } catch (ClassNotFoundException | IOException classNotFoundException) {
                    classNotFoundException.printStackTrace();
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

        ImageIcon imageIcon = new ImageIcon(main.getClass().getResource("/项目.png"));
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
}
