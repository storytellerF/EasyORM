import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;

public class EntityTableModel extends AbstractTableModel {
    Field[] fields;
    @Override
    public int getRowCount() {
        if (fields==null) return 0;
        return fields.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        if (column==0){
            return "列名";
        }else {
            return "类型";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex==0){
            return fields[rowIndex].getType().getSimpleName();
        }else {
            return fields[rowIndex].getName();
        }
    }
}
