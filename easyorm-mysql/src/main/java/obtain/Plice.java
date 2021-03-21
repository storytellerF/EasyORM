package obtain;

import query.Insert;
import query.Select;
import query.Update;

import javax.sql.DataSource;

public class Plice {
    MySqlDatabase mySqlDatabase;

    public Plice(DataSource dataSource) {
        mySqlDatabase=new MySqlDatabase(dataSource);
    }


    public <T> Select<T> getSelect() {
        return new Select<>(mySqlDatabase);
    }
    public <T> Select<T> getSelect(Class<T> tClass) {
        return new Select<T>(mySqlDatabase).select(tClass);
    }

    public <T>Update<T> getUpdate(){
        return new Update<>(mySqlDatabase);
    }
    public <T>Insert<T> getInsert(){
        return new Insert<>(mySqlDatabase);
    }
}
