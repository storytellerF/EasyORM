import com.gui.main.ConnectionConfig;
import com.gui.main.CreateConfig;
import com.gui.view.DatabaseConnectionInput;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class Projector {
    private JPanel panel1;
    private JButton 新建一个连接Button;
    private JButton 退出Button;
    private JList listInDatabase;
    private JList listInModel;
    private JList listColumnDetailInDatabase;
    private JList listColumnDetailInModel;
    private JButton state;
    private DatabaseConnectionInput databaseConnectionInput;
    private Connection connection;

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
                    CreateConfig createConfig = CreateConfig.build(statement,config,connection);
                    Set<String> strings = createConfig.getTables().keySet();
                    for (String key : strings) {
                        listInDatabase.add(new JLabel(key));
                    }
//                    ParseDatabase create = new ParseDatabase(createConfig);
//                    create.parseDatabase(databaseConnectionInput.isEnableLomok());
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(panel1,e1.getMessage()!=null?e1.getMessage():e1, "Error", JOptionPane.ERROR_MESSAGE);
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
