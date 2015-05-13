package cn.dgg.CRM365.domain.resourcesManage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dgg_wrongclient")
public class WrongClient {
	private Long id;//id
	private String inputTime;//录入日期
	private String clientMsg;//客户信息
	private String area;//省市区域
	private String clientAdd;//客户地址
	private String clientTel;//客户联系方式
	private String spareTel1;//备用电话1
	private String spareTel2;//备用电话2
	private String clientStatus;//客户状态
	private String source;//客户来源
	private String remark;//备注
	
	public WrongClient() {
		
	}
	public WrongClient(String inputTime, String clientMsg, String area,
			String clientAdd, String clientTel, String spareTel1,
			String spareTel2, String source) {
		this.inputTime = inputTime;
		this.clientMsg = clientMsg;
		this.area = area;
		this.clientAdd = clientAdd;
		this.clientTel = clientTel;
		this.spareTel1 = spareTel1;
		this.spareTel2 = spareTel2;
		this.clientStatus = "未签单";
		this.source = source;
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
	@Column(name = "inputTime")
	public String getInputTime() {
		return inputTime;
	}
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
	@Column(name = "clientMsg")
	public String getClientMsg() {
		return clientMsg;
	}
	public void setClientMsg(String clientMsg) {
		this.clientMsg = clientMsg;
	}
	@Column(name = "clientAdd")
	public String getClientAdd() {
		return clientAdd;
	}
	public void setClientAdd(String clientAdd) {
		this.clientAdd = clientAdd;
	}
	@Column(name = "clientTel")
	public String getClientTel() {
		return clientTel;
	}
	public void setClientTel(String clientTel) {
		this.clientTel = clientTel;
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
	@Column(name = "area")
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	@Column(name = "source")
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
