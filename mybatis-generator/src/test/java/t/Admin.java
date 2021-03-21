package t;

import annotation.RealName;
import annotation.constraint.Nullable;
import annotation.constraint.PrimaryKey;
import annotation.type.exact.IntColumn;
import annotation.type.string.VarcharColumn;

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
}