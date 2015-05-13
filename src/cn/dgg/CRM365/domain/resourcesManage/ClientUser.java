package cn.dgg.CRM365.domain.resourcesManage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 客户信息和用户的中间表
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  黄剑锋
  * @version  [版本号, Dec 20, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@Entity
@Table(name="dgg_clientUser")
public class ClientUser {
	private Long id;//id
	private Long client;//客户信息
	private Long assignFrom;//客户拥有人
	private Long assigner;//分配用户
	private Long assignTo;//分配至
	private String assignDate;//分配时间
	@Id
	@Column(name = "id")
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "client")
	public Long getClient() {
		return client;
	}
	public void setClient(Long client) {
		this.client = client;
	}
	
	@Column(name = "assigner")
	public Long getAssigner() {
		return assigner;
	}
	public void setAssigner(Long assigner) {
		this.assigner = assigner;
	}
	
	@Column(name = "assignTo")
	public Long getAssignTo() {
		return assignTo;
	}
	public void setAssignTo(Long assignTo) {
		this.assignTo = assignTo;
	}
	
	@Column(name = "assignDate")
	public String getAssignDate() {
		return assignDate;
	}
	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
	}
	
	@Column(name = "assignFrom")
	public Long getAssignFrom() {
		return assignFrom;
	}
	public void setAssignFrom(Long assignFrom) {
		this.assignFrom = assignFrom;
	}
}
