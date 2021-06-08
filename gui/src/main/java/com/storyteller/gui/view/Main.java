package com.storyteller.gui.view;

import com.storyteller.gui.main.ClassLoaderManager;
import com.storyteller.gui.model.ConnectionConfig;
import com.storyteller.util.RUtil;
import com.storyteller_f.relay_message.RelayMessage;
import com.storyteller_f.uiscale.UIUtil;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        UIUtil.setFont(start, produceComponent, readExcel, saveButton, reflectToCode, reflectToDatabase, reviewAllModel, produceStaticFieldModel);
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
            windowWait();
            RelayMessage relayMessage = RUtil.reflectToCode(connection, databaseConnectionInput);
            windowRelease();
            dialogShowMessage(relayMessage);
        });

        start.addActionListener(e -> {
            try {
                windowWait();
                ConnectionConfig config = databaseConnectionInput.getCreateConfig();
                connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
                start.setBackground(Color.GREEN);
                windowRelease();
            } catch (SQLException exception) {
                windowRelease();
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
            windowWait();
            RelayMessage relayMessage = RUtil.reflectToDatabase(databaseConnectionInput, connection);
            windowRelease();
            dialogShowMessage(relayMessage);

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
            windowWait();
            RelayMessage relayMessage = RUtil.produceStaticFunction(databaseConnectionInput);
            windowRelease();
            dialogShowMessage(relayMessage);
        });
        //endregion
    }

    private void dialogShowMessage(RelayMessage relayMessage) {
        JOptionPane.showMessageDialog(contentPanel,relayMessage.message);
    }

    private void windowRelease() {
        contentPanel.setCursor(Cursor.getDefaultCursor());
    }

    private void windowWait() {
        contentPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                main.destroy();
            }
        });
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

    private void destroy() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean checkDatabaseConnection() {
        if (connection == null) {
            JOptionPane.showMessageDialog(contentPanel, "未连接数据库");
            return true;
        }
        return false;
    }
}
