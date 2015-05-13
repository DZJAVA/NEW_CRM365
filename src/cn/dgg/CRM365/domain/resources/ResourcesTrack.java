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


/**
 * 资源列表 <功能简述> <功能详细描述>
 * 
 * @author wangqiang
 * @version [版本号, Aug 12, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 资源跟踪
 */
@Entity
@Table(name = "dgg_resourcestrack")
public class ResourcesTrack {
	private Long rtid;//ID
	private String resourcescontent;//跟踪内容
	private String resourcestime;//跟踪时间
	private User resourcespeople;//跟踪人（淘汰人）（签单人）
	private String remark;// 签单备注
	private Client  client_name;//客户（资源客户名称）
	private String intoasinglerate;//成单率80% 50% 0% 10% 100%
	private String workplan;//工作计划
	private String calltime;//上门时间
	private String types;//类型（1已上门，2未上门）
	private String plantime;//计划工作 时间
	private String biaoshi; //还款管理的ID 
		
	@Id
	@Column(name = "rtid")
	@GeneratedValue
	public Long getRtid() {
		return rtid;
	}
	public void setRtid(Long rtid) {
		this.rtid = rtid;
	}
	@Column(name = "resourcescontent")
	public String getResourcescontent() {
		return resourcescontent;
	}
	public void setResourcescontent(String resourcescontent) {
		this.resourcescontent = resourcescontent;
	}
	@Column(name = "resourcestime")
	public String getResourcestime() {
		return resourcestime;
	}
	public void setResourcestime(String resourcestime) {
		this.resourcestime = resourcestime;
	}
	@ManyToOne
	@JoinColumn(name = "resourcespeople")
	public User getResourcespeople() {
		return resourcespeople;
	}
	public void setResourcespeople(User resourcespeople) {
		this.resourcespeople = resourcespeople;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@ManyToOne
	@JoinColumn(name = "client_name")
	public Client getClient_name() {
		return client_name;
	}
	public void setClient_name(Client client_name) {
		this.client_name = client_name;
	}
	@Column(name = "intoasinglerate")
	public String getIntoasinglerate() {
		return intoasinglerate;
	}
	public void setIntoasinglerate(String intoasinglerate) {
		this.intoasinglerate = intoasinglerate;
	}
	@Column(name = "workplan")
	public String getWorkplan() {
		return workplan;
	}
	public void setWorkplan(String workplan) {
		this.workplan = workplan;
	}
	@Column(name = "calltime")
	public String getCalltime() {
		return calltime;
	}
	public void setCalltime(String calltime) {
		this.calltime = calltime;
	}
	@Column(name = "types")
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	
	@Column(name = "plantime")
	public String getPlantime() {
		return plantime;
	}
	public void setPlantime(String plantime) {
		this.plantime = plantime;
	}
	@Column(name = "biaoshi")
	public String getBiaoshi() {
		return biaoshi;
	}
	public void setBiaoshi(String biaoshi) {
		this.biaoshi = biaoshi;
	}
	}