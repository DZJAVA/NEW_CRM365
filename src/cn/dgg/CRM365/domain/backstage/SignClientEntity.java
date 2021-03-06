package cn.dgg.CRM365.domain.backstage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 客户信息管理
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  黄剑锋
  * @version  [版本号, Dec 19, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@Entity
@Table(name="sign_client")
public class SignClientEntity {
	private Integer id;//id
	private String signDate;
	private String clientCode;
	private String loanType;
	private String loanBank;
	private String loanSource;
	private Integer follower;
	private int client;
	private String followDate;
	private String followInfo;
	private Integer status;
	private String backDate;
	private String loanAmount;//贷款金额
	private String dataList;
	private String dataField;
	private String unCommitData;
	
	@Column(name = "dataList")
	public String getDataList() {
		return dataList;
	}
	public void setDataList(String dataList) {
		this.dataList = dataList;
	}
	
	@Column(name = "dataField")
	public String getDataField() {
		return dataField;
	}
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}
	
	@Column(name = "unCommitData")
	public String getUnCommitData() {
		return unCommitData;
	}
	public void setUnCommitData(String unCommitData) {
		this.unCommitData = unCommitData;
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "signDate")
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	
	@Column(name = "clientCode")
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	
	@Column(name = "loanType")
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	
	@Column(name = "loanBank")
	public String getLoanBank() {
		return loanBank;
	}
	public void setLoanBank(String loanBank) {
		this.loanBank = loanBank;
	}
	
	@Column(name = "loanSource")
	public String getLoanSource() {
		return loanSource;
	}
	public void setLoanSource(String loanSource) {
		this.loanSource = loanSource;
	}
	
	@Column(name = "follower")
	public Integer getFollower() {
		return follower;
	}
	public void setFollower(Integer follower) {
		this.follower = follower;
	}
	
	@Column(name = "followDate")
	public String getFollowDate() {
		return followDate;
	}
	public void setFollowDate(String followDate) {
		this.followDate = followDate;
	}
	
	@Column(name = "followInfo")
	public String getFollowInfo() {
		return followInfo;
	}
	public void setFollowInfo(String followInfo) {
		this.followInfo = followInfo;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "backDate")
	public String getBackDate() {
		return backDate;
	}
	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}
	
	@Column(name = "client")
	public int getClient() {
		return client;
	}
	public void setClient(int client) {
		this.client = client;
	}
	
	@Column(name = "loanAmount")
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
}
