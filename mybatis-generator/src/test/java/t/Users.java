package t;

import com.storyteller_f.sql_query.annotation.RealName;
import com.storyteller_f.sql_query.annotation.constraint.Nullable;
import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.annotation.type.exact.IntColumn;
import com.storyteller_f.sql_query.annotation.type.string.CharColumn;
import com.storyteller_f.sql_query.annotation.type.string.VarcharColumn;

public class Users {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSelfshow() {
        return selfshow;
    }

    public void setSelfshow(String selfshow) {
        this.selfshow = selfshow;
    }

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