package t;

import com.storyteller_f.sql_query.annotation.RealName;
import com.storyteller_f.sql_query.annotation.constraint.Nullable;
import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.annotation.type.exact.IntColumn;
import com.storyteller_f.sql_query.annotation.type.string.VarcharColumn;

public class Admin {
    @IntColumn
    @PrimaryKey
    @RealName(name = "管理员ID")
    private int id;
    @VarcharColumn(length = 20)
    @Nullable
    @RealName(name = "管理员帐号")
    private String username;
    @VarcharColumn(length = 20)
    @Nullable
    @RealName(name = "管理员密码")
    private String password;
    @VarcharColumn(length = 20)
    @Nullable
    @RealName(name = "真是姓名")
    private String name;
    @IntColumn
    @Nullable
    @RealName(name = "权限")
    private int qx;
    public static String s_id="id";
    public static String s_username="username";
    public static String s_password="password";
    public static String s_name="name";
    public static String s_qx="qx";
}