package test_model;

import java.util.Date;

import com.storyteller_f.sql_query.annotation.Children;
import com.storyteller_f.sql_query.annotation.EnumRemark;
import com.storyteller_f.sql_query.annotation.Table;
import com.storyteller_f.sql_query.annotation.constraint.PrimaryKey;
import com.storyteller_f.sql_query.annotation.type.string.EnumColumn;

@Table(name = "User")
public class User {
	@EnumColumn({"admin","user"})
	@EnumRemark({"管理员","普通用户"})
	private String level;
	@PrimaryKey
	private String name;
	private int age;
	private Date registerTime;
	private String remark;
	@Children
	private IdCard idCard;

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
	public static String name() {
		return "name";
	}
	public static String age() {
		return "age";
	}
	public static String registerTime() {
		return "registerTime";
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
		return "User [name=" + name + ", age=" + age + ", registeTime=" + registerTime + "]";
	}
	public static String s_name ="name";
	public static String s_age ="age";
	public static String s_registerTime ="registeTime";
	public static String s_remark="remark";
	
}
