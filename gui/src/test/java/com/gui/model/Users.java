package com.gui.model;

import annotation.type.exact.IntColumn;
import annotation.constraint.PrimaryKey;
import annotation.RealName;
import annotation.type.string.VarcharColumn;
import annotation.constraint.Nullable;
import annotation.type.string.CharColumn;

public class Users {
    @IntColumn
    @PrimaryKey
    @RealName(name = "用户ID")
    private int id;
    @VarcharColumn(length = 20)
    @Nullable
    @RealName(name = "用户帐号")
    private String username;
    @VarcharColumn(length = 20)
    @Nullable
    @RealName(name = "用户密码")
    private String password;
    @VarcharColumn(length = 20)
    @Nullable
    @RealName(name = "真是姓名")
    private String name;
    @VarcharColumn(length = 30)
    @Nullable
    @RealName(name = "用户昵称")
    private String nic;
    @CharColumn(length = 2)
    @Nullable
    @RealName(name = "性别")
    private String sex;
    @IntColumn
    @RealName(name = "年龄")
    private int age;
    @VarcharColumn(length = 30)
    @RealName(name = "电子邮件")
    private String email;
    @VarcharColumn(length = 20)
    @RealName(name = "电话")
    private String phone;
    @VarcharColumn(length = 300)
    @RealName(name = "个人说明")
    private String selfshow;
}