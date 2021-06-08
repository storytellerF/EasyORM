import com.storyteller.gui.model.ConnectionConfig;
import com.storyteller_f.uiscale.UIUtil;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryWindow {
    private final DatabaseQueryResultModel resultModel = new DatabaseQueryResultModel();
    JPanel panel1;
    JSplitPane splitPanel;
    private JTextArea queryTextPanel;
    private JTextPane plainResultPanel;
    private JButton runButton;
    private JTabbedPane resultPanel1;
    private JTable tableResultPanel;
    private ConnectionConfig config;

    public QueryWindow(ConnectionConfig config) {
        UIUtil.setFont(runButton, queryTextPanel, plainResultPanel, resultPanel1);
        this.config = config;
        tableResultPanel.setModel(resultModel);
        runButton.addActionListener(e -> {
            String text = queryTextPanel.getText();
            if (text.trim().length() > 0) {
                try {
                    Connection connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
                    Statement statement = connection.createStatement();
                    long start=System.currentTimeMillis();
                    boolean execute = statement.execute(text);
                    long span=System.currentTimeMillis()-start;
                    StringBuilder stringBuilder=new StringBuilder("is result set:"+execute + "\n");
                    stringBuilder.append("耗时：").append(span).append("ms");
                    if (execute) {
                        resultPanel1.setSelectedIndex(1);
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
                            List<String> values = new ArrayList<>();
                            for (int i = 0; i < columnCount; i++) {
                                String string = resultSet.getString(i + 1);
                                values.add(string);
                            }
                            resultModel.addColumnValue(values);
                        }
                        resultSet.close();
                        System.out.println("refresh");
                        resultModel.fireTableStructureChanged();
                    } else {
                        resultPanel1.setSelectedIndex(0);
                        stringBuilder.append("受影响的行数：").append(statement.getUpdateCount());
                    }

                    plainResultPanel.setText(stringBuilder.toString());
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
        panel1.setSize(200, 200);
    }
}
