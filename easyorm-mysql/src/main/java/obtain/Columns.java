package obtain;

import java.util.ArrayList;
import java.util.List;

public class Columns {
//    private List<String> name = new ArrayList<>();
//    private List<String> type = new ArrayList<>();
    private final String[] name;
    private final String[] type;

    public Columns(int count) {
        name=new String[count];
        type=new String[count];
    }

    public void add(int index,String name, String type) {
        this.name[index]=name;
        this.type[index]=type;
    }

    public String getName(int index) {
        return name[index];
    }

    public String getType(int index) {
        return type[index];
    }
}
