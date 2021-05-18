import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryResultModel extends AbstractTableModel {
    public List<String> columnNameList =new ArrayList<>();
    public List<List<String>> columnValueList =new ArrayList<>();

    public void clearColumnName() {
        columnNameList.clear();
    }
    public void addColumnName(String name) {
        columnNameList.add(name);
    }

    public void clearColumnValue() {
        columnValueList.clear();
    }

    public void addColumnValue(List<String> value) {
        columnValueList.add(value);
    }
    @Override
    public int getRowCount() {
        return columnValueList.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNameList.get(column);
    }

    @Override
    public int getColumnCount() {
        return columnNameList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return columnValueList.get(rowIndex).get(columnIndex);
    }
}
