import com.storyteller.gui.main.ClassLoaderManager;
import com.storyteller.gui.model.ConnectionConfig;
import com.storyteller.gui.main.TableConfig;
import com.storyteller.gui.model.InformationSchemaColumn;
import com.storyteller.gui.model.Table;
import com.storyteller.gui.view.DatabaseConnectionInput;
import com.storyteller_f.uiscale.DataZone;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

public class Projector {
    DatabaseTableModel databaseTableModel;
    EntityTableModel entityTableModel;
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
    private JButton reflectToModel;
    private JButton reflectToDatabase;
    private JTable table1;
    private JTable table2;
    private DatabaseConnectionInput databaseConnectionInput;
    private Connection connection;
    private TableConfig tableConfig;
    private ConnectionConfig config;

    public Projector() {
        DataZone.setFont(queryButton, exitButton, newConnectionButton, stateButton, databaseStatusLabel,
                listInDatabase, listInModel, table1, table2);
        databaseTableModel = new DatabaseTableModel();
        entityTableModel = new EntityTableModel();
        table2.setModel(databaseTableModel);
        table1.setModel(entityTableModel);
        newConnectionButton.addActionListener(e -> {
            GetConnectionDialog dialog = new GetConnectionDialog();
            dialog.pack();
            dialog.setVisible(true);
            System.out.println("database name:"+ dialog.getDatabaseInput().getDatabaseName());
            if (!dialog.isOk) return;
            panel1.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            databaseConnectionInput = dialog.getDatabaseInput();
            try {
                config = databaseConnectionInput.getCreateConfig();
                connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
                stateButton.setText("连接成功");
            } catch (SQLException exception) {
                //exception.printStackTrace();
                JOptionPane.showMessageDialog(panel1, exception.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                stateButton.setText("连接失败");
                panel1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }
            //显示数据库中的表
            if (!databaseConnectionInput.checkAll()) {
                panel1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                return;
            }

            try {
                Statement statement = connection.createStatement();
                ConnectionConfig config = databaseConnectionInput.getCreateConfig();
                tableConfig = TableConfig.build(statement, config, connection);
                databaseTableModel.config = tableConfig;
                Set<String> strings = tableConfig.getTables().keySet();
                String[] keys = new String[strings.size()];
                int index = 0;
                for (String key : strings) {
                    System.out.println(key);
                    keys[index++] = key;
                }
                listInDatabase.setListData(keys);
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(panel1, e1.getMessage() != null ? e1.getMessage() : e1, "Error", JOptionPane.ERROR_MESSAGE);
            }
            //显示模型
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
                String[] strings = new String[files.length];
                int index = 0;
                for (File child : files) {
                    if (child.isFile()) {
                        strings[index++] = child.getName();
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
            databaseTableModel.setSelected(selectedValue);
            databaseTableModel.fireTableStructureChanged();
//            HashMap<String, Table> tables = tableConfig.getTables();
//            Table table = tables.get(selectedValue);
//            String[] c = new String[table.getColumns().size()];
//            int index = 0;
//            for (InformationSchemaColumn column : table.getColumns()) {
//                c[index++] = column.getName() + " " + column.getType();
//            }
//            listColumnDetailInDatabase.setListData(c);
        });
        listInModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                return;
            }
            String classFile = listInModel.getSelectedValue();
            String className = classFile.substring(0, classFile.indexOf('.'));
            try {
                Field[] fields = Class.forName(databaseConnectionInput.packageName() + "." + className).getDeclaredFields();
                entityTableModel.fields = fields;
                entityTableModel.fireTableStructureChanged();
//                String[] strings = new String[fields.length];
//                int index = 0;
//                for (Field field : fields) {
//                    strings[index++] = field.getName() + " " + field.getType();
//                }
//                listColumnDetailInModel.setListData(strings);
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        queryButton.addActionListener(e -> {
            QueryWindow queryWindow = new QueryWindow(config);
            JFrame jFrame = new JFrame("Query");
            jFrame.setContentPane(queryWindow.panel1);
            jFrame.setVisible(true);
            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            jFrame.setSize(400, 400);
            queryWindow.splitPanel.setDividerLocation(200);
        });
        databaseSplitPanel.setDividerLocation(100);
        modelSpliePanel.setDividerLocation(100);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Projector projector = new Projector();
        JFrame jFrame = new JFrame("projector");
        jFrame.setContentPane(projector.panel1);
        jFrame.setVisible(true);
        jFrame.setSize(500, 400);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                projector.destroy();
            }
        });
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void destroy() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
