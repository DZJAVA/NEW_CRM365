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
@Table(name="loan_source")
public class LoanSource {
	private Integer id;//id
	private String loanDate;
	private String loanYear;
	private String loanInterest;
	private String serviceFee;
	private String interestType;
	private int signClient;
	private String sourceName;
	private String loanAmount;
	private int status;
	private String receiveAmount;
	private String sourceAmount;
	
	@Column(name = "sourceAmount")
	public String getSourceAmount() {
		return sourceAmount;
	}
	public void setSourceAmount(String sourceAmount) {
		this.sourceAmount = sourceAmount;
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
	
	@Column(name = "status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(name = "loanDate")
	public String getLoanDate() {
		return loanDate;
	}
	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}
	
	@Column(name = "loanYear")
	public String getLoanYear() {
		return loanYear;
	}
	public void setLoanYear(String loanYear) {
		this.loanYear = loanYear;
	}
	
	@Column(name = "loanInterest")
	public String getLoanInterest() {
		return loanInterest;
	}
	public void setLoanInterest(String loanInterest) {
		this.loanInterest = loanInterest;
	}
	
	@Column(name = "serviceFee")
	public String getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}
	
	@Column(name = "interestType")
	public String getInterestType() {
		return interestType;
	}
	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}
	
	@Column(name = "signClient")
	public int getSignClient() {
		return signClient;
	}
	public void setSignClient(int signClient) {
		this.signClient = signClient;
	}
	
	@Column(name = "sourceName")
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	@Column(name = "loanAmount")
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	
	@Column(name = "receiveAmount")
	public String getReceiveAmount() {
		return receiveAmount;
	}
	public void setReceiveAmount(String receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
}
