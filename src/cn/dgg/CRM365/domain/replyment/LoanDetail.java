package cn.dgg.CRM365.domain.replyment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.dgg.CRM365.domain.resourcesManage.Client;

/**
 * 贷款给与方式明细
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  chenqin
  * @version  [版本号, Dec 17, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@Entity
@Table(name = "dgg_loanDetail")
public class LoanDetail {
	
	private Long id;
	private Client client;	//客户资源外键
	private String type;	//给与类型
	private String sum;		//给与金额大小
	private String rcount;	//还款期数
	private String monthMoney;	//每月还款金额
	private String refundTotal;	//还款总金额
	private String rremark;	//备注
	private String hkuanTime;//还款时间
	
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
	@JoinColumn(name = "client")
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "sum")
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	@Column(name = "rcount")
	public String getRcount() {
		return rcount;
	}
	public void setRcount(String rcount) {
		this.rcount = rcount;
	}
	@Column(name = "monthMoney")
	public String getMonthMoney() {
		return monthMoney;
	}
	public void setMonthMoney(String monthMoney) {
		this.monthMoney = monthMoney;
	}
	@Column(name = "refundTotal")
	public String getRefundTotal() {
		return refundTotal;
	}
	public void setRefundTotal(String refundTotal) {
		this.refundTotal = refundTotal;
	}
	@Column(name = "rremark")
	public String getRremark() {
		return rremark;
	}
	public void setRremark(String rremark) {
		this.rremark = rremark;
	}
	@Column(name = "payBackTime")
	public String getHkuanTime() {
		return hkuanTime;
	}
	public void setHkuanTime(String hkuanTime) {
		this.hkuanTime = hkuanTime;
	}

}
