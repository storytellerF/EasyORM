package com.storyteller.gui.view;


import com.config_editor.view.ConfigEditorUI;
import com.storyteller_f.sql_query.annotation.RealName;
import com.storyteller_f.sql_query.annotation.constraint.Nullable;
import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.config_editor.model.Config;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.storyteller.gui.main.ColumnsToField;
import com.storyteller.gui.model.ExcelReadConfig;
import com.storyteller.gui.model.validate.CustomField;
import com.storyteller_f.uiscale.UIUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadExcelGetModel {
    private final HashMap<String, Integer> selectedHashMap = new HashMap<>();
    private final List<String> typeList = new ArrayList<>();
    private JPanel jPanel;
    private JTextField headerNamePosition;
    private JTextField filePath;
    private JButton selectFile;
    private JTextField sheet1TextField;
    private JTextField columnCount22;
    private JButton startButton;
    private JPanel cells;
    private JTextField rowCount;
    private JButton parseButton;
    private JTextPane textPane1;
    private ConfigEditorUI excelConfig;
    private JButton saveButton;
    private String path;
    private XSSFSheet sheet;
    private JFrame jFrame;
    private boolean isInitial;

    public ReadExcelGetModel() {
        typeList.add("type");
        typeList.add("name");
        typeList.add("realName");
        typeList.add("comment");
        typeList.add("enum");
        typeList.add("nullable");
        typeList.add("restrain");
        UIUtil.setFont(headerNamePosition, filePath, selectFile, sheet1TextField, columnCount22, startButton, cells, rowCount,
                parseButton, textPane1, saveButton);
        ComboShow comboShow = new ComboShow();
        ComboSelected comboSelected = new ComboSelected();
        selectFile.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".xlsx");
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.setSelectedFile(new File("C:\\Users\\lava\\Desktop\\工作簿1.xlsx"));
            int result = jFileChooser.showOpenDialog(jPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                path = jFileChooser.getSelectedFile().getAbsolutePath();
                filePath.setText(path);
            }
        });
        startButton.addActionListener(e -> preStart(comboShow, comboSelected));
        parseButton.addActionListener(e -> parse());
        saveButton.addActionListener(e -> excelConfig.save());
        initEditor();
        JTextField[] jTextField = new JTextField[]{
                rowCount,
                columnCount22,
                headerNamePosition,
                filePath,
                sheet1TextField
        };
        for (JTextField j : jTextField) {
            j.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    dnib();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    dnib();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
        }
    }

    //    private void loopHash() {
//        System.out.println("遍历开始");
//        Set<String> strings = selectedHashMap.keySet();
//        for (String string : strings) {
//            Integer integer = selectedHashMap.get(string);
//            System.out.println(string+" "+ integer);
//        }
//        System.out.println("遍历结束");
//    }
    public void bind(ExcelReadConfig excelReadConfig) {
        synchronized (this) {
            isInitial = true;
        }
        rowCount.setText(excelReadConfig.getRowCount() + "");
        columnCount22.setText(excelReadConfig.getColumnCount() + "");
        filePath.setText(excelReadConfig.getPath());
        path = excelReadConfig.getPath();
        headerNamePosition.setText(excelReadConfig.getTableHeaderRow() + "");
        sheet1TextField.setText(excelReadConfig.getSheetName());
        synchronized (this) {
            isInitial = false;
        }
    }

    //todo 内容发生改变时调用
    public void dnib() {
        synchronized (this) {
            if (isInitial) {
                return;
            }
        }
        Config current = excelConfig.getCurrent();
        if (current instanceof ExcelReadConfig) {
            ExcelReadConfig creatorConfig = (ExcelReadConfig) current;
            creatorConfig.setPath(filePath.getText());
            creatorConfig.setSheetName(sheet1TextField.getText());
            try {

                creatorConfig.setRowCount(Integer.parseInt(rowCount.getText()));
                creatorConfig.setColumnCount(Integer.parseInt(columnCount22.getText()));
                creatorConfig.setTableHeaderRow(Integer.parseInt(headerNamePosition.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initEditor() {
        excelConfig.setListener(new ConfigEditorUI.ConfigEditorListener() {
            @Override
            public void onShow(Config config) {
                if (config instanceof ExcelReadConfig) {
                    ExcelReadConfig excelReadConfig =
                            (ExcelReadConfig) config;
                    bind(excelReadConfig);
                }
            }

            @Override
            public Config onNew() {
                ExcelReadConfig creatorConfig =
                        new ExcelReadConfig();
                creatorConfig.setName("未命名" + System.currentTimeMillis());
                return creatorConfig;
            }
        });
        RuntimeTypeAdapterFactory<Config> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Config.class)
                .registerSubtype(ExcelReadConfig.class);
        try {
            excelConfig.init("com.storyteller.gui.read_excel", runtimeTypeAdapterFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void preStart(ComboShow comboShow, ComboSelected comboSelected) {
        if (path != null && notEmpty(headerNamePosition) && notEmpty(columnCount22)) {
            FileInputStream fileInputStream;
            XSSFWorkbook sheets = null;

            try {
                fileInputStream = new FileInputStream(path);
                sheets = new XSSFWorkbook(fileInputStream);
                // 获取sheet
                sheet = sheets.getSheet(sheet1TextField.getText());
                XSSFRow xssfRow = sheet.getRow(Integer.parseInt(headerNamePosition.getText()));
                int count = Integer.parseInt(columnCount22.getText());
                cells.removeAll();
                for (int j = 0; j < count; j++) {
                    XSSFCell text = xssfRow.getCell(j);
                    if (text != null) {
                        System.out.println(text);
                        {
                            JLabel comp = new JLabel();
                            comp.setText(text.getStringCellValue());
                            GridBagConstraints labelConstraints = new GridBagConstraints();
                            labelConstraints.fill = GridBagConstraints.CENTER;
                            labelConstraints.gridx = j;
                            labelConstraints.gridy = 0;
                            cells.add(comp, labelConstraints);
                        }
                        {
                            GridBagConstraints comboBoxConstraints = new GridBagConstraints();
                            comboBoxConstraints.gridx = j;
                            comboBoxConstraints.gridy = 1;
                            JComboBox<String> jComboBox = new JComboBox<>();
                            jComboBox.addItem("未选择");
                            for (String string : typeList) {
                                if (!selectedHashMap.containsKey(string)) {
                                    jComboBox.addItem(string);
                                }
                            }
                            jComboBox.setName(Integer.toString(j));
                            jComboBox.addPopupMenuListener(comboShow);
                            jComboBox.addItemListener(comboSelected);
                            cells.add(jComboBox, comboBoxConstraints);
                        }

                    } else {
                        System.out.println("text is null");
                    }
                }
                jFrame.pack();

            } catch (Exception numberFormatException) {
                numberFormatException.printStackTrace();
            } finally {
                try {
                    if (sheets != null) {
                        sheets.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    private void parse() {
        if (path != null && notEmpty(headerNamePosition) && notEmpty(columnCount22)
                && notEmpty(rowCount)) {
            File file=new File(path);
            FileInputStream fileInputStream = null;
            XSSFWorkbook sheets = null;
            try {
                fileInputStream = new FileInputStream(path);
                sheets = new XSSFWorkbook(fileInputStream);
                // 获取sheet
                sheet = sheets.getSheet(sheet1TextField.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            String fileName = file.getName();
            int endIndex = fileName.indexOf('.');
            if (endIndex<0) {
                JOptionPane.showMessageDialog(jPanel,"指定的文件可能不是一个Excel 文件");
                return;
            }
            String className= fileName.substring(0, endIndex);
            try {
                int starter = Integer.parseInt(headerNamePosition.getText());
                int row = Integer.parseInt(rowCount.getText());
                CustomField last = null;
                boolean add = true;
                ColumnsToField cusColumnsToField = new ColumnsToField("", className);
                for (int i = starter + 1; i < row; i++) {
                    XSSFRow xssfRow = sheet.getRow(i);
                    if (xssfRow == null) {
                        JOptionPane.showMessageDialog(jPanel, "设定的行数可能不存在");
                        break;
                    }

                    CustomField customField = new CustomField();
                    XSSFCell type = xssfRow.getCell(selectedHashMap.get("type"));
                    XSSFCell real = xssfRow.getCell(selectedHashMap.get("realName"));
                    Integer nullableIndex = selectedHashMap.get("nullable");
                    if (nullableIndex != null) {
                        XSSFCell nullable = xssfRow.getCell(nullableIndex);
                        if (nullable.getStringCellValue().equals("可以")) {
                            customField.add("@Nullable");
                            customField.add(Nullable.class);
                        }
                    }
                    XSSFCell name = xssfRow.getCell(selectedHashMap.get("name"));
                    String nameValue = name.getStringCellValue();
                    String typeValue = type.getStringCellValue();
                    customField.nameAndType(nameValue, typeValue);
                    Integer enumIndex = selectedHashMap.get("enum");
                    if (enumIndex != null) {
                        XSSFCell enumXss = xssfRow.getCell(enumIndex);
                        if (typeValue.length() > 0) {//包含类型
                            getEnumInfo(customField, enumXss);
                        }
                        if (last != null && typeValue.length() == 0) {//不包含类型，而且直到上一次的
                            getEnumInfo(last, enumXss);//添加到上次的
                            add = false;//不需要添加新的对象
                        }
                    }
                    XSSFCell restrainXss = xssfRow.getCell(selectedHashMap.get("restrain"));
                    if (restrainXss != null) {
                        String value = restrainXss.getStringCellValue();
                        if (value.equals("主键")) {
                            customField.add("@PrimaryKey");
                            customField.add(PrimaryKey.class);
                        } else if (value.equals("非空")) {
                            customField.add("@Nullable");
                            customField.add(Nullable.class);
                        }
                    }
                    customField.add("@RealName( name =\"" + real.getStringCellValue() + "\")");
                    customField.add(RealName.class);
                    if (add) {
                        cusColumnsToField.add(customField);//添加新的对象
                        last = customField;//保存
                    } else {
                        add = true;//下一次需要添加对象，除非又出现了类型为空的
                    }

                }

                textPane1.setText(cusColumnsToField.content());
            } catch (Exception numberFormatException) {
                numberFormatException.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                try {
                    if (sheets != null) {
                        sheets.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(jPanel, "未填写完全");
        }
    }

    private boolean notEmpty(JTextField rowCount) {
        return rowCount.getText().trim().length() > 0;
    }

    private <E> JComboBox<E> parseToNew(JComboBox<?> jComboBox, @SuppressWarnings("SameParameterValue") Class<E> c) {
        JComboBox<E> output = new JComboBox<>();
        output.setName(jComboBox.getName());
        for (int i = 0; i < jComboBox.getItemCount(); i++) {
            Object object = jComboBox.getItemAt(i);
            if (object != null && c.isAssignableFrom(object.getClass())) {
                output.addItem(c.cast(object));
            }
        }
        return output;
    }

    public String[] getEnum(String content) {
        return content.split("[:：]", 2);
    }

    private void getEnumInfo(CustomField last, XSSFCell enumXss) {
        if (enumXss.getStringCellValue().length() > 0) {
            String[] keyAndValue = getEnum(enumXss.getStringCellValue());
            if (keyAndValue.length > 0) {
                last.put(keyAndValue[0], keyAndValue[1]);
            }
        }
    }

    public void show() {
        jFrame = new JFrame("读取Excel 文件");
        jFrame.setContentPane(jPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    class ComboShow implements PopupMenuListener {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            if (e.getSource() instanceof JComboBox<?>) {
                //noinspection unchecked
                JComboBox<String> jComboBox = (JComboBox<String>) e.getSource();
                jComboBox.removeAllItems();
                jComboBox.addItem("未选择");
                for (String string : typeList) {
                    if (!selectedHashMap.containsKey(string)) {
                        jComboBox.addItem(string);
                    }
                }
            }

        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {

        }
    }

    class ComboSelected implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e12) {
            if (e12.getSource() instanceof JComboBox<?>) {
                JComboBox<String> j1 = parseToNew((JComboBox<?>) e12.getSource(), String.class);
                String name = j1.getName();
                if (e12.getStateChange() == ItemEvent.SELECTED) {
                    if (!e12.getItem().equals("未选择")) {
                        if (selectedHashMap.containsKey(e12.getItem().toString())) {
                            selectedHashMap.replace((String) e12.getItem(),
                                    Integer.parseInt(name));
                        } else {
                            selectedHashMap.put((String) e12.getItem(),
                                    Integer.parseInt(name));
                        }
                    }
                } else {
                    if (!e12.getItem().equals("未选择")) {
                        selectedHashMap.remove(e12.getItem().toString());
                    }

                }

            }

        }
    }
}
