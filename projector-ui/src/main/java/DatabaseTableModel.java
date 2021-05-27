import com.storyteller.gui.main.TableConfig;
import com.storyteller.gui.model.InformationSchemaColumn;
import com.storyteller.gui.model.Table;

import javax.swing.table.AbstractTableModel;

public class DatabaseTableModel extends AbstractTableModel {
    TableConfig config;
    String selected;
    Table table;

    public void setSelected(String selected) {
        this.selected = selected;
        table = config.getTables().get(selected);
    }

    @Override
    public int getRowCount() {
        if (table==null) return 0;
        return table.getColumns().size();
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
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InformationSchemaColumn informationSchemaColumn = table.getColumns().get(rowIndex);
        if (columnIndex==0){
            return informationSchemaColumn.getName();
        }else {
            return informationSchemaColumn.getType();
        }
    }
}
