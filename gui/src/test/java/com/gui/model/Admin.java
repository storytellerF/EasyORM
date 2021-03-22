package com.gui.model;

import com.storyteller_f.sql_query.annotation.NoQuery;
import com.storyteller_f.sql_query.annotation.type.exact.IntColumn;
import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.annotation.RealName;
import com.storyteller_f.sql_query.annotation.type.string.VarcharColumn;
import com.storyteller_f.sql_query.annotation.constraint.Nullable;

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
    @RealName(name = "真实姓名")
    private String name;
    @IntColumn
    @Nullable
    @RealName(name = "权限")
    private int qx;
    @NoQuery
    public static String column_id = "id";
}