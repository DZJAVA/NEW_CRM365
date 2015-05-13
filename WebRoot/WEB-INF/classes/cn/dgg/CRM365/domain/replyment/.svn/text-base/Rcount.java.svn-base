package cn.dgg.CRM365.domain.replyment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 还款期数管理
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  chenqin
  * @version  [版本号, Dec 18, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@Entity
@Table(name = "dgg_Rcount")
public class Rcount {
	
	private Long id ;
	private LoanDetail loanDetail;	//贷款明细
	private String number;	//第几期
	private String payTime;	//还款时间
	private String rSum; 	//还款金额
	private String factRTime;	//实际还款时间
	private String status;	//	状态 0为未还款 1 为已还款
	private String remark;
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name = "loanDetail")
	public LoanDetail getLoanDetail() {
		return loanDetail;
	}
	public void setLoanDetail(LoanDetail loanDetail) {
		this.loanDetail = loanDetail;
	}
	@Column(name = "number")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	@Column(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "rSum")
	public String getRSum() {
		return rSum;
	}
	public void setRSum(String sum) {
		rSum = sum;
	}
	@Column(name = "factRTime")
	public String getFactRTime() {
		return factRTime;
	}
	public void setFactRTime(String factRTime) {
		this.factRTime = factRTime;
	}
	@Column(name = "payTime")
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

}
