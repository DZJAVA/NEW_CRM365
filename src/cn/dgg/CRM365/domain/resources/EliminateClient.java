package cn.dgg.CRM365.domain.resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.resourcesManage.ClientSource;

@Entity
@Table(name="dgg_eliminateclient")
public class EliminateClient {
	private Long id;//id
	private String clientName;//客户名称
	private String contactTel;//客户联系方式
	private String loanAmount;//贷款金额
	private String clientAdd;//客户地址
	private String oppType;//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
	private String spareTel1;//备用电话1
	private String spareTel2;//备用电话2
	private String clientStatus;//客户状态(1为已签单、2为未签单、3为淘汰 4,退单)
	private String remark;//备注
	private String signPossible;//签单可能性(1为100%、2为80%、3为50%、4为0%)
	private String assignDate;//录入日期
	private String assignTime;//分配时间
	private User follower;//跟单人
	private ClientSource clientSourse;//客户来源
	private String eliTime;//淘汰时间
	
	private String province;//省份
	private String city;//城市
	
	@Column(name = "eliTime")
	public String getEliTime() {
		return eliTime;
	}
	public void setEliTime(String eliTime) {
		this.eliTime = eliTime;
	}
	@ManyToOne
	@JoinColumn(name = "follower")
	public User getFollower() {
		return follower;
	}
	public void setFollower(User follower) {
		this.follower = follower;
	}
	@Column(name = "assignDate")
	public String getAssignDate() {
		return assignDate;
	}
	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
	}
	@Column(name = "assignTime")
	public String getAssignTime() {
		return assignTime;
	}
	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
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
	@Column(name = "clientName")
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	@Column(name = "contactTel")
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	@Column(name = "loanAmount")
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	@Column(name = "clientAdd")
	public String getClientAdd() {
		return clientAdd;
	}
	public void setClientAdd(String clientAdd) {
		this.clientAdd = clientAdd;
	}
	@Column(name = "oppType")
	public String getOppType() {
		return oppType;
	}
	public void setOppType(String oppType) {
		this.oppType = oppType;
	}
	@Column(name = "spareTel1")
	public String getSpareTel1() {
		return spareTel1;
	}
	public void setSpareTel1(String spareTel1) {
		this.spareTel1 = spareTel1;
	}
	@Column(name = "spareTel2")
	public String getSpareTel2() {
		return spareTel2;
	}
	public void setSpareTel2(String spareTel2) {
		this.spareTel2 = spareTel2;
	}
	@Column(name = "clientStatus")
	public String getClientStatus() {
		return clientStatus;
	}
	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "signPossible")
	public String getSignPossible() {
		return signPossible;
	}
	public void setSignPossible(String signPossible) {
		this.signPossible = signPossible;
	}
	@ManyToOne
	@JoinColumn(name = "clientSourse")
	public ClientSource getClientSourse() {
		return clientSourse;
	}
	public void setClientSourse(ClientSource clientSourse) {
		this.clientSourse = clientSourse;
	}
	@Column(name = "province")
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	@Column(name = "city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
