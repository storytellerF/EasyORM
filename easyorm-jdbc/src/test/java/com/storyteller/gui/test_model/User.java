package com.storyteller.gui.test_model;

import com.storyteller_f.sql_query.annotation.Children;
import com.storyteller_f.sql_query.annotation.EnumRemark;
import com.storyteller_f.sql_query.annotation.Table;
import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.annotation.type.string.EnumColumn;

import java.util.Date;

@Table(name = "User")
public class User {

    public static String static_name = "name";
    public static String static_age = "age";
    public static String static_registerTime = "registerTime";
    public static String static_remark = "remark";
    @EnumColumn({"admin", "user"})
    @EnumRemark({"管理员", "普通用户"})
    private String level;
    @PrimaryKey
    private String name;
    private int age;
    private Date registerTime;
    private String remark;
    @Children
    private IdCard idCard;

    public static String name() {
        return "name";
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public IdCard getIdCard() {
        return idCard;
    }

    public void setIdCard(IdCard idCard) {
        this.idCard = idCard;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "level='" + level + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", registerTime=" + registerTime +
                ", remark='" + remark + '\'' +
                ", idCard=" + idCard +
                '}';
    }
}
