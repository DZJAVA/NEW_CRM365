package cn.dgg.CRM365.domain.resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.resourcesManage.Client;

@Entity
@Table(name="dgg_eliminate")
public class Eliminate {
	private Long id;
	private Client client;//淘汰客户
	private User user;//淘汰用户
	private String eliTime;//淘汰时间
	private String remark;//备注
	
	@Id 
	@Column(name = "id")
	@GeneratedValue
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
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Column(name = "eliTime")
	public String getEliTime() {
		return eliTime;
	}
	public void setEliTime(String eliTime) {
		this.eliTime = eliTime;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
