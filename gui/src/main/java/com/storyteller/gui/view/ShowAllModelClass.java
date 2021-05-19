package com.storyteller.gui.view;

import com.storyteller.gui.main.ClassLoaderManager;
import com.storyteller_f.uiscale.DataZone;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Field;

public class ShowAllModelClass {
    protected JTree jTree;
    private JPanel contentPanel;
    private JButton generateForm;
    private JScrollPane scrollPanel;
    private String packageName;

    public ShowAllModelClass() {
        DataZone.setFont(generateForm,jTree);
        contentPanel.add(scrollPanel, BorderLayout.CENTER);
        generateForm.addActionListener(e -> gotoClass());
        for (int i = 0; i < 10; i++) {
            char c = (char) ('0' + i);
            KeyStroke keyStroke = KeyStroke.getKeyStroke(c);
            contentPanel.getInputMap().put(keyStroke, "quick");
        }
        contentPanel.getActionMap().put("quick", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                int length = actionCommand.length();
                if (length == 1) {
                    char keyChar = actionCommand.charAt(0);
                    if (keyChar <= '9' && keyChar >= '0') {
                        System.out.println("char:" + keyChar);
                        jTree.setSelectionRow((int) (keyChar - '0'));
                    }
                }

            }
        });
        contentPanel.getInputMap().put(KeyStroke.getKeyStroke('\n'), "generate");
        contentPanel.getActionMap().put("generate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gotoClass();
            }
        });

    }

    private void gotoClass() {
        if (jTree != null) {
            Object ob = jTree.getLastSelectedPathComponent();
            if (ob instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode selected = ((DefaultMutableTreeNode) ob);
                int depth = selected.getPath().length;
                System.out.println("深度：" + depth);
                if (depth == 2) {
                    String className = selected.getUserObject().toString();
                    System.out.println(className);
                    GenerateHTMLFormElement generateHTMLFormElement = new GenerateHTMLFormElement();
                    generateHTMLFormElement.show();
                    generateHTMLFormElement.init();
                    generateHTMLFormElement.create(className, packageName);
                }

            }
        } else {
            System.out.println("jTree is null");
        }
    }

    public void showModel(String modelPath, String packageName) {
        this.packageName = packageName;
        System.out.println(modelPath + " " + packageName);
        ClassLoaderManager.getInstance().oneStep(modelPath, packageName);
        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(modelPath);
        jTree = new JTree(defaultMutableTreeNode);
        scrollPanel.setViewportView(jTree);
        addToTree(new File(modelPath), defaultMutableTreeNode, packageName);
        jTree.expandRow(0);
//        jTree.addTreeSelectionListener(e -> {
//            System.out.println("row:"+ Arrays.toString(jTree.getSelectionRows()));
//            System.out.println("rowCount:"+jTree.getRowCount());
//            System.out.println("depth:"+jTree.getSelectionPath().getPath().length);
//        });
    }

    private void addToTree(File file, DefaultMutableTreeNode defaultMutableTreeNode, String packageName) {

        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File child : files) {
            if (child.isFile()) {
                DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(child.getName());
                defaultMutableTreeNode.add(classNode);
                addLeaf(classNode, child.getName(), packageName);
            } else {
                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(file.getName());
                defaultMutableTreeNode.add(newChild);
                addToTree(child, newChild, packageName);
            }
        }
    }

    private void addLeaf(DefaultMutableTreeNode node, String name, String packageName) {
        System.out.println(name + " " + packageName);
        try {

            String className = name.substring(0, name.indexOf('.'));
            Class<?> clazz = Class.forName(packageName + "." + className);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                node.add(new DefaultMutableTreeNode(field.getName()));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        JFrame jFrame = new JFrame("所有模型类信息");
        jFrame.setContentPane(contentPanel);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}
