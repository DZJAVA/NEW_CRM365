package cn.dgg.CRM365.domain.authority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 用户信息
 * 
 * @author chenqin
 * 
 */
@Entity
@Table(name = "dgg_user")
public class User {

	private Long id;
	private String userName;// 员工姓名（预留字段）
	private String sex;// 性别 0 男 1 女（预留字段）
	private String loginId;// 登录账号
	private String password;// 密码
	private String telPhone;// 用户电话号码（预留字段）
	private int isOrNotEnable;// 用户是否停用 1 停用 2 启用
	private String remark;
	private Role role;// 角色对象
	private int userDelState;//删除状态,0未删除,1已删除
	private Department department;
	private int counts;
	private int signStatus;
	
	public User() {
		
	}

	public User(Long id, String userName, String loginId, String password,
			Role role, Long deptId, String deptname, int superid, Integer isFront) {
		this.id = id;
		this.userName = userName;
		this.loginId = loginId;
		this.password = password;
		this.role = role;
		Department d = new Department();
		d.setId(deptId);
		d.setDepaName(deptname);
		d.setSuperId(superid);
		d.setIsFront(isFront);
		this.department = d;
	}

	@Column(name = "counts")
	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}
	@Column(name = "signStatus")
	public int getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(int signStatus) {
		this.signStatus = signStatus;
	}

	@ManyToOne
	@JoinColumn(name = "role")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "userName")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "loginId")
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Column(name = "userSex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@ManyToOne
	@JoinColumn(name = "department")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Column(name = "isOrNotEnable")
	public int getIsOrNotEnable() {
		return isOrNotEnable;
	}

	public void setIsOrNotEnable(int isOrNotEnable) {
		this.isOrNotEnable = isOrNotEnable;
	}

	@Column(name = "telPhone")
	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	@Column(name = "userDelState")
	public int getUserDelState() {
		return userDelState;
	}

	public void setUserDelState(int userDelState) {
		this.userDelState = userDelState;
	}

}
