package com.storyteller.gui.view;

import com.storyteller.gui.main.ClassLoaderManager;
import com.storyteller_f.uiscale.DataZone;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Field;

public class ShowAllModelClass {
    private JPanel contentPanel;
    private JButton generateForm;
    private JScrollPane scrollPanel;
    private JTree jTree;
    private String packageName;

    public ShowAllModelClass() {
        DataZone.setFont(jTree,generateForm);
        contentPanel.add(scrollPanel, BorderLayout.CENTER);
        generateForm.addActionListener(e -> openGenerateHTMLFormWindow());
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
                        jTree.setSelectionRow(keyChar - '0');
                    }
                }

            }
        });
        contentPanel.getInputMap().put(KeyStroke.getKeyStroke('\n'), "generate");
        contentPanel.getActionMap().put("generate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGenerateHTMLFormWindow();
            }
        });
        jTree.setModel(null);
    }

    private void openGenerateHTMLFormWindow() {
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
    }

    public void showModel(String modelPath, String packageName) {
        this.packageName = packageName;
        System.out.println(modelPath + " " + packageName);
        boolean b = ClassLoaderManager.getInstance().oneStep(modelPath, packageName);
        if (!b) {
            JOptionPane.showMessageDialog(contentPanel,"编译失败");
            return;
        }
        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(modelPath);
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(defaultMutableTreeNode);
        jTree.setModel(defaultTreeModel);
        jTree.treeDidChange();
        addToTree(new File(modelPath), defaultMutableTreeNode, packageName);
        jTree.expandRow(0);
    }

    private void addToTree(File file, DefaultMutableTreeNode defaultMutableTreeNode, String packageName) {
        System.out.println("addToTree:"+packageName);
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
                addToTree(child, newChild, packageName+"."+child.getName());
            }
        }
    }

    private void addLeaf(DefaultMutableTreeNode node, String name, String packageName) {
        System.out.println("addLeaf:"+name + " " + packageName);
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
