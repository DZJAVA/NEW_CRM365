package cn.dgg.CRM365.domain.resourcesManage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.dgg.CRM365.domain.authority.User;


/**
 * 新增客户 自动分配     
 * 根据客户来源分配
 * 
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  黄剑锋
  * @version  [版本号, Dec 19, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@Entity
@Table(name="dgg_xzallocation")
public class XzAllocation {
	
	private Long id;//ID
	private User userid;//用户ID
	private String csourceid;//客户来源ID
	
	@Id
	@Column(name = "id")
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "csourceid")
	public String getCsourceid() {
		return csourceid;
	}
	public void setCsourceid(String csourceid) {
		this.csourceid = csourceid;
	}
	@ManyToOne
	@JoinColumn(name="userid")
	public User getUserid() {
		return userid;
	}
	public void setUserid(User userid) {
		this.userid = userid;
	}

}
