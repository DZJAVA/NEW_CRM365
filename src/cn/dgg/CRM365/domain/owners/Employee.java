package cn.dgg.CRM365.domain.owners;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.dgg.CRM365.domain.authority.Department;

/**
 * 员工表 <功能简述> <功能详细描述>
 * 
 * @author chenqin
 * @version [版本号, Aug 9, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity 
@Table(name = "dgg_employee")
public class Employee {
	private Long id;
	private String name;// 名字
	private String sex;// 性别（预留字段）
	private String age;// 年龄（预留字段）
	private String birthday;//生日
	private Department department;// 部门
	private String idCardNum;//身份证号码
	private String mailbox;// 邮箱
	private String IDcard;// 手机号码
	private String marriage;//婚姻状况,1已婚，2未婚
	private String address;//地址
	private String state;//在职状态（1实习、2见习、3正式）
	private String counts;//细数
	private String signStatus;//签单状态(0为不签单、1为签单)
	private String delState;//删除状态,0未删除,1已删除
	private String remark;// 备注
	

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "department")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "age")
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}


	@Column(name = "IDcard")
	public String getIDcard() {
		return IDcard;
	}

	public void setIDcard(String dcard) {
		IDcard = dcard;
	}

	@Column(name = "sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}





	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "signStatus")
	public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	
	@Column(name = "counts")
	public String getCounts() {
		return counts;
	}

	public void setCounts(String counts) {
		this.counts = counts;
	}
	@Column(name = "mailbox")
	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}
	@Column(name = "idCardNum")
	public String getIdCardNum() {
		return idCardNum;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}
	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "birthday")
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	@Column(name = "marriage")
	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	@Column(name = "state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	@Column(name = "delState")
	public String getDelState() {
		return delState;
	}

	public void setDelState(String delState) {
		this.delState = delState;
	}
	
}
