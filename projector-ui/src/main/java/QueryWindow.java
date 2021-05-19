import com.storyteller.gui.main.ConnectionConfig;
import com.storyteller_f.uiscale.DataZone;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryWindow {
    JPanel panel1;
    private JTextArea textArea1;
    private JTextPane textPane1;
    private JButton runButton;
    private JTabbedPane tabbedPane1;
    private JTable table1;
    JSplitPane splitPanel;
    private ConnectionConfig config;
    private final DatabaseQueryResultModel resultModel=new DatabaseQueryResultModel();
    public QueryWindow(ConnectionConfig config) {
        DataZone.setFont(runButton,textArea1,textPane1,tabbedPane1);
        this.config=config;
        table1.setModel(resultModel);
        runButton.addActionListener(e -> {
            String text = textArea1.getText();
            if (text.trim().length() > 0) {
                try {
                    Connection connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
                    Statement statement = connection.createStatement();
                    boolean execute = statement.execute(text);
                    if (execute) {
                        tabbedPane1.setSelectedIndex(1);
                        ResultSet resultSet = statement.getResultSet();
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        resultModel.clearColumnName();
                        resultModel.clearColumnValue();
                        for (int i = 0; i < columnCount; i++) {
                            String columnName = metaData.getColumnName(i + 1);
                            resultModel.addColumnName(columnName);
                        }
                        while (resultSet.next()) {
                            List<String> values=new ArrayList<>();
                            for (int i = 0; i < columnCount; i++) {
                                String string = resultSet.getString(i+1);
                                values.add(string);
                            }
                            resultModel.addColumnValue(values);
                        }
                        resultSet.close();
                        System.out.println("refresh");
                        resultModel.fireTableStructureChanged();
                    }else{
                        tabbedPane1.setSelectedIndex(1);
                    }

                    textPane1.setText(execute+"\n");
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
        panel1.setSize(200,200);
    }
}
