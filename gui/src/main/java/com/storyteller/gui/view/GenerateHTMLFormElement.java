package com.storyteller.gui.view;

import com.storyteller_f.sql_query.annotation.RealName;
import com.storyteller_f.sql_query.annotation.type.string.EnumColumn;
import com.config_editor.model.Config;
import com.config_editor.view.ConfigEditor;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.storyteller.gui.createHtml.BootstrapType;
import com.storyteller.gui.createHtml.DiTing;
import com.storyteller.gui.createHtml.HTMLFormItem;
import com.storyteller.gui.createHtml.RegularType;
import com.storyteller.gui.main.FileState;
import com.storyteller.gui.model.HTMLCreatorConfig;
import com.storyteller_f.uiscale.DataZone;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GenerateHTMLFormElement {
    private final List<String> charsetList;
    FileState fileState = new FileState();
    boolean isInitial = false;
    private JPanel contentPanel;
    private JTextPane produceResultPanel;
    private JButton copyToButton;
    private HTMLFormItem htmlFormItem;
    private int verifyIndex;
    private JTabbedPane tabbedPane1;
    private JRadioButton bootstrapNoVerifyRadioButton;
    private JRadioButton bootstrapSystemVerifyRadioButton;
    private JRadioButton bootstrapDiTingVerifyRadioButton;
    private JRadioButton regularNoVerifyRadioButton;
    private JRadioButton regularSystemVerifyRadioButton;
    private JRadioButton regularDiTingVerifyRadioButton;
    private JScrollPane leftScrollPanel;
    private JTextField filePath;
    private JButton openFileButton;
    private JComboBox<String> charsetComboBox;
    private JSplitPane splitPanel;
    private ConfigEditor htmlConfigEditor;
    private JButton button1;
    private JFrame jFrame;

    public GenerateHTMLFormElement() {
        DataZone.setFont(produceResultPanel,copyToButton,openFileButton,filePath,charsetComboBox,button1);
        setTab(4);
        charsetList = new ArrayList<>();
        try {
            EnumColumn charset = HTMLCreatorConfig.class.getDeclaredField("charset").getAnnotation(EnumColumn.class);
            for (String s : charset.value()) {
                charsetList.add(s);
                charsetComboBox.addItem(s);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(contentPanel, "基本不会出现的情况，快去买彩票吧");
        }
        fileState.setListener(state -> {
            if (state) {
                openFileButton.setText("ok");
            } else {
                openFileButton.setText("error");
            }
        });

        bootstrapNoVerifyRadioButton.addItemListener(e -> {
            htmlFormItem = new BootstrapType(0, "class-left", "class-right");
            verifyIndex = 0;
            dnib();
        });
        bootstrapSystemVerifyRadioButton.addItemListener(e -> {
            htmlFormItem = new BootstrapType(1, "class-left", "class-right");
            verifyIndex = 1;
        });
        bootstrapDiTingVerifyRadioButton.addItemListener(e -> {
            htmlFormItem = new BootstrapType(0, "class-left", "class-right");
            verifyIndex = 2;
        });
        regularNoVerifyRadioButton.addItemListener(e -> {
            htmlFormItem = new RegularType(0);
            verifyIndex = 0;
        });
        regularSystemVerifyRadioButton.addItemListener(e -> {
            htmlFormItem = new RegularType(1);
            verifyIndex = 1;
        });
        regularDiTingVerifyRadioButton.addItemListener(e -> {
            htmlFormItem = new RegularType(0);
            verifyIndex = 2;
        });
        JRadioButton[] jRadioButtons = new JRadioButton[]{
                regularNoVerifyRadioButton,
                regularSystemVerifyRadioButton,
                regularDiTingVerifyRadioButton,
                bootstrapSystemVerifyRadioButton,
                bootstrapDiTingVerifyRadioButton,
                bootstrapNoVerifyRadioButton
        };
        for (JRadioButton jRadioButton : jRadioButtons) {
            jRadioButton.addItemListener((e -> dnib()));
        }
        copyToButton.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = new StringSelection(produceResultPanel.getText());
            clipboard.setContents(transferable, null);
        });
        openFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setSelectedFile(new File("E:\\测试\\index.html"));
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int chooserResult = jFileChooser.showOpenDialog(contentPanel);
            if (chooserResult == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jFileChooser.getSelectedFile();
                filePath.setText(selectedFile.getAbsolutePath());
                fileState.open(selectedFile);
                bindPath();
            }
        });
        charsetComboBox.addItemListener(e -> dnib());
        button1.addActionListener(e -> htmlConfigEditor.save());
    }

    public void bindPath() {
        Config current = htmlConfigEditor.getCurrent();
        if (current instanceof HTMLCreatorConfig) {
            HTMLCreatorConfig creatorConfig = (HTMLCreatorConfig) current;
            creatorConfig.setPath(filePath.getText());
        }
    }

    public void setTab(int w) {
        FontMetrics fm = produceResultPanel.getFontMetrics(produceResultPanel.getFont());
        int charWidth = fm.charWidth(' ');
        int tabWidth = charWidth * w;
        TabStop[] stop = new TabStop[5];
        for (int i = 0; i < stop.length; i++) {
            stop[i] = new TabStop((i + 1) * tabWidth);
        }
        TabSet tabSet = new TabSet(stop);
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setTabSet(attributeSet, tabSet);
        int length = produceResultPanel.getDocument().getLength();
        produceResultPanel.getStyledDocument().setParagraphAttributes(0, length, attributeSet, false);
    }

    private String getHtml(Field field, int index) throws Exception {
        String realNameString;
        if (field.isAnnotationPresent(RealName.class)) {
            RealName realName = field.getAnnotation(RealName.class);
            realNameString = (realName.name());
        } else {
            realNameString = (index + "");
        }
        String name = field.getName();
        return htmlFormItem.parse(name, realNameString, field);
    }

    //todo 内容发生改变时调用
    public void dnib() {
        synchronized (this) {
            if (isInitial) {
                return;
            }
        }
        Config current = htmlConfigEditor.getCurrent();
        if (current instanceof HTMLCreatorConfig) {
            HTMLCreatorConfig creatorConfig = (HTMLCreatorConfig) current;
            creatorConfig.setPath(filePath.getText());
            creatorConfig.setCharset(charsetComboBox.getItemAt(charsetComboBox.getSelectedIndex()));
            int verity;
            if (tabbedPane1.getSelectedIndex() == 0) {
                creatorConfig.setType("bootstrap");
                if (bootstrapNoVerifyRadioButton.isSelected()) {
                    verity = 0;
                } else if (bootstrapSystemVerifyRadioButton.isSelected()) {
                    verity = 1;
                } else {
                    verity = 2;
                }
            } else {
                creatorConfig.setType("regular");
                if (regularNoVerifyRadioButton.isSelected()) {
                    verity = 0;
                } else if (regularSystemVerifyRadioButton.isSelected()) {
                    verity = 1;
                } else {
                    verity = 2;
                }
            }
            creatorConfig.setVerify(verity);
        }
    }

    public void bind(HTMLCreatorConfig creatorConfig) {
        synchronized (this) {
            isInitial = true;
        }
        fileState.open(creatorConfig.getPath());
        verifyIndex = creatorConfig.getVerify();
        filePath.setText(creatorConfig.getPath());
        if (creatorConfig.getType().equals("bootstrap")) {
            if (creatorConfig.getVerify() == 0) {
                bootstrapNoVerifyRadioButton.setSelected(true);
            } else if (creatorConfig.getVerify() == 1) {
                bootstrapSystemVerifyRadioButton.setSelected(true);
            } else {
                bootstrapDiTingVerifyRadioButton.setSelected(true);
            }
            tabbedPane1.setSelectedIndex(0);
        } else {
            if (creatorConfig.getVerify() == 0) {
                regularNoVerifyRadioButton.setSelected(true);
            } else if (creatorConfig.getVerify() == 1) {
                regularSystemVerifyRadioButton.setSelected(true);
            } else {
                regularDiTingVerifyRadioButton.setSelected(true);
            }
            tabbedPane1.setSelectedIndex(1);
        }
        String charset = creatorConfig.getCharset();
        charsetComboBox.setSelectedIndex(charsetList.indexOf(charset));
        synchronized (this) {
            isInitial = false;
        }
    }

    public void initEditor() {
        htmlConfigEditor.setListener(new ConfigEditor.ConfigEditorListener() {
            @Override
            public void onInit(Config config) {
                if (config instanceof HTMLCreatorConfig) {
                    HTMLCreatorConfig creatorConfig =
                            (HTMLCreatorConfig) config;
                    bind(creatorConfig);
                }
            }

            @Override
            public Config onNew() {
                HTMLCreatorConfig creatorConfig =
                        new HTMLCreatorConfig();
                creatorConfig.setName("未命名" + System.currentTimeMillis());
                return creatorConfig;
            }
        });
        RuntimeTypeAdapterFactory<Config> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Config.class)
                .registerSubtype(HTMLCreatorConfig.class);
        try {
            htmlConfigEditor.init("com.storyteller.gui.generate_html", runtimeTypeAdapterFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void create(String classFile, String packageName) {
        try {
            String className = classFile.substring(0, classFile.indexOf('.'));
            Field[] fields = Class.forName(packageName + "." + className).getDeclaredFields();
            int max = 0;
            for (Field f : fields) {
                int length = f.toString().length();
                if (length > max) {
                    max = length;
                }
            }
            JList<Field> jList = new JList<>(fields);
            jFrame.setSize(max * 10 + 400, 400);
            splitPanel.setDividerLocation(max * 10);
            jList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    return;
                }
                try {
                    List<Field> fieldList = jList.getSelectedValuesList();
                    int i = 0;
                    StringBuilder htmlForm = new StringBuilder("\n");
                    StringBuilder script = new StringBuilder("<script>\n");
                    StringBuilder diTingBuilder = new StringBuilder("let d = $(\"#{id}\").diting({\r\n\trulers : {\r\n");

                    for (Field field : fieldList) {
                        htmlForm.append(getHtml(field, i)).append("\n");
                        String fieldName = field.getName();
                        script.append("\t").append(htmlFormItem.getScript(fieldName)).append("\n");
                        if (verifyIndex == 2) {
                            DiTing diTing = new DiTing(field);
                            diTingBuilder.append(diTing.parse());
                        }
                    }
                    diTingBuilder.append("})");
                    htmlForm.append("\n");

                    if (fileState.accept()) {
                        //开始替换
                        FileReader fileReader = new FileReader(fileState.getPath());
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;

                        StringBuilder stringBuilder = new StringBuilder();
                        boolean appendable = true;
                        while ((line = bufferedReader.readLine()) != null) {
                            String prefix = "<!-- formContent[";
                            String suffix = "] -->";
                            String lineNew = line.trim();
                            if (lineNew.startsWith(prefix) && lineNew.endsWith(suffix)) {
                                //获取表单的id
                                String id = lineNew.substring(prefix.length(), lineNew.length() - suffix.length());

                                stringBuilder.append(line).append("\n");
                                //输入表单内容
                                if (id.equals("end")) {
                                    appendable = true;
                                } else {
                                    System.out.println("表单的id:" + id);
                                    if (verifyIndex == 2) {
                                        diTingBuilder.replace(12, 16, id);
                                        System.out.println(diTingBuilder.toString());
                                        script.append(diTingBuilder.toString());
                                        script.append("</script>\n");
                                    }
                                    stringBuilder.append(htmlForm.toString()).append(script.toString());
                                    appendable = false;
                                }

                            } else if (appendable) {
                                stringBuilder.append(line).append("\n");
                            } else {
                                System.out.print("不添加");
                            }
                            System.out.println();
                        }
                        bufferedReader.close();
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileState.getPath()));
                        bufferedWriter.write(stringBuilder.toString());
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    } else {
                        if (verifyIndex == 2) {
                            script.append(diTingBuilder.toString());
                        }
                        script.append("</script>\n");
                    }
                    produceResultPanel.setText(htmlForm.toString() + script.toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(contentPanel, e1.getMessage());
                }

            });

            leftScrollPanel.setViewportView(jList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void init() {
        htmlFormItem = new BootstrapType(0, "class-left", "class-right");
        tabbedPane1.setSelectedIndex(0);
        verifyIndex = 0;
        initEditor();
    }

    public void show() {
        jFrame = new JFrame("生成html 表单");
        jFrame.setContentPane(contentPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setVisible(true);
//        jFrame.setSize(600,400);
    }
}
