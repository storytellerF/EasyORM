import com.storyteller.gui.main.ClassLoaderManager;
import com.storyteller.gui.main.ConnectionConfig;
import com.storyteller.gui.main.CreateConfig;
import com.storyteller.gui.model.InformationSchemaColumn;
import com.storyteller.gui.model.Table;
import com.storyteller.gui.view.DatabaseConnectionInput;
import com.storyteller_f.uiscale.DataZone;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Projector {
    private JPanel panel1;
    private JButton newConnectionButton;
    private JButton exitButton;
    private JList<String> listInDatabase;
    private JList<String> listInModel;
    private JList<String> listColumnDetailInDatabase;
    private JList<String> listColumnDetailInModel;
    private JButton stateButton;
    private JButton queryButton;
    private JSplitPane databaseAndModel;
    private JSplitPane databaseSplitPanel;
    private JSplitPane modelSpliePanel;
    private JLabel databaseStatusLabel;
    private DatabaseConnectionInput databaseConnectionInput;
    private Connection connection;
    private CreateConfig createConfig;
    private ConnectionConfig config;
    public Projector() {
        DataZone.setFont(queryButton,exitButton,newConnectionButton,stateButton,databaseStatusLabel,
                listInDatabase,listColumnDetailInDatabase,listInModel,listColumnDetailInModel);
//        System.out.println(file.getAbsolutePath());
        newConnectionButton.addActionListener(e -> {
            GetConnectionDialog dialog = new GetConnectionDialog();
            dialog.pack();
            dialog.setVisible(true);
            System.out.println(dialog.getDatabaseInput().getDatabaseName());
            if (!dialog.isOk) return;
            panel1.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            databaseConnectionInput= dialog.getDatabaseInput();
            try {
                config = databaseConnectionInput.getCreateConfig();
                connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
                stateButton.setText("连接成功");
            } catch (SQLException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(panel1, exception.getMessage());
                stateButton.setText("连接失败");
                panel1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                return;
            }
            //显示数据库中的表
//                if (checkDatabaseConnection()) return;
            if (!databaseConnectionInput.checkAll()) {
                panel1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                return;
            }

            try {
                Statement statement = connection.createStatement();
                ConnectionConfig config = databaseConnectionInput.getCreateConfig();
                createConfig = CreateConfig.build(statement,config,connection);
                Set<String> strings = createConfig.getTables().keySet();
                String[] keys=new String[strings.size()];
                int index=0;
                for (String key : strings) {
                    System.out.println(key);
                    keys[index++]=key;
                }
                listInDatabase.setListData(keys);
//                    ParseDatabase create = new ParseDatabase(createConfig);
//                    create.parseDatabase(databaseConnectionInput.isEnableLomok());
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(panel1,e1.getMessage()!=null?e1.getMessage():e1, "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (databaseConnectionInput.checkModel()) {
                boolean b = ClassLoaderManager.getInstance().oneStep(databaseConnectionInput.getModel(), databaseConnectionInput.packageName());
                if (!b) {
                    int re = JOptionPane.showConfirmDialog(panel1, "编译失败，是否清除缓存（清楚之后可以再次进行编译）", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if (re == JOptionPane.YES_OPTION) {
                        ClassLoaderManager.getInstance().init();
                    }
                    return;
                }
                File[] files = new File(databaseConnectionInput.getModel()).listFiles();
                if (files == null) {
                    panel1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
                String[] strings=new String[files.length];
                int index=0;
                for (File child : files) {
                    if (child.isFile()) {
                        strings[index++]=child.getName();
                    } else {
                        //文件夹暂时不添加
                    }
                }
                listInModel.setListData(strings);
            }

            panel1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        });
        listInDatabase.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                return;
            }
            String selectedValue = listInDatabase.getSelectedValue();
            HashMap<String, Table> tables = createConfig.getTables();
            Table table= tables.get(selectedValue);
            String[] c=new String[table.getColumns().size()];
            int index=0;
            for (InformationSchemaColumn column : table.getColumns()) {
                c[index++]=column.getName()+" "+column.getType();
            }
            listColumnDetailInDatabase.setListData(c);
        });
        listInModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                return;
            }
            String classFile = listInModel.getSelectedValue();
            String className = classFile.substring(0, classFile.indexOf('.'));
            try {
                Field[] fields = Class.forName(databaseConnectionInput.packageName() + "." + className).getDeclaredFields();
                String[] strings=new String[fields.length];
                int index=0;
                for (Field field : fields) {
                    strings[index++]=field.getName()+" "+field.getType();
                }
                listColumnDetailInModel.setListData(strings);
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        queryButton.addActionListener(e -> {
            QueryWindow queryWindow=new QueryWindow(config);
            JFrame jFrame=new JFrame("Query");
            jFrame.setContentPane(queryWindow.panel1);
            jFrame.setVisible(true);
            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            jFrame.setSize(400,400);
            queryWindow.splitPanel.setDividerLocation(200);
        });
        databaseSplitPanel.setDividerLocation(100);
        modelSpliePanel.setDividerLocation(100);
    }

    public static void main(String[] args) {
        Projector projector=new Projector();
        JFrame jFrame=new JFrame("projector");
        jFrame.setContentPane(projector.panel1);
        jFrame.setVisible(true);
        jFrame.setSize(500,400);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
