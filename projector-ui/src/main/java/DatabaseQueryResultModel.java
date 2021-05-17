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
        System.out.println("getRowCount"+columnValueList.size());
        return columnValueList.size();
    }

    @Override
    public String getColumnName(int column) {
        System.out.println("getColumnName"+column);
        return columnNameList.get(column);
    }

    @Override
    public int getColumnCount() {
        System.out.println("getColumnCount"+columnNameList.size());
        return columnNameList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        System.out.println("getValueAt"+rowIndex+" "+columnIndex);
        return columnValueList.get(rowIndex).get(columnIndex);
    }
}
