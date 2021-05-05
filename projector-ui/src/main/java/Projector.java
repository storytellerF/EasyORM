import com.gui.main.ClassLoaderManager;
import com.gui.main.ConnectionConfig;
import com.gui.main.CreateConfig;
import com.gui.model.InformationSchemaColumn;
import com.gui.model.Table;
import com.gui.view.DatabaseConnectionInput;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

public class Projector {
    private JPanel panel1;
    private JButton 新建一个连接Button;
    private JButton 退出Button;
    private JList<String> listInDatabase;
    private JList<String> listInModel;
    private JList<String> listColumnDetailInDatabase;
    private JList<String> listColumnDetailInModel;
    private JButton state;
    private DatabaseConnectionInput databaseConnectionInput;
    private Connection connection;
    private CreateConfig createConfig;

    public Projector() {
        新建一个连接Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetConnectionDialog dialog = new GetConnectionDialog();
                dialog.pack();
                dialog.setVisible(true);
                System.out.println(dialog.getDatabaseInput().getDatabaseName());
                databaseConnectionInput= dialog.getDatabaseInput();
                try {
                    ConnectionConfig config = databaseConnectionInput.getCreateConfig();
                    connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
                    state.setText("连接成功");
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(panel1, exception.getMessage());
                    state.setText("连接失败");
                    return;
                }
                //显示数据库中的表
//                if (checkDatabaseConnection()) return;
                if (!databaseConnectionInput.checkAll()) return;

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
                    ClassLoaderManager.getInstance().oneStep(databaseConnectionInput.getModel(), databaseConnectionInput.packageName());
                    File[] files = new File(databaseConnectionInput.getModel()).listFiles();
                    if (files == null) {
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
            }
        });
        listInDatabase.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
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
            }
        });
        listInModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
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
            }
        });
    }

    public static void main(String[] args) {
        Projector projector=new Projector();
        JFrame jFrame=new JFrame("projector");

        jFrame.setContentPane(projector.panel1);
        jFrame.setVisible(true);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
