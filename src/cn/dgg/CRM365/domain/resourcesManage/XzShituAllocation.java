package cn.dgg.CRM365.domain.resourcesManage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 视图
 *  查询条件 ： 根据接单状态。是否启用 。是否删除。 部门接单状态
 *  查询结果： 用户ID 
 * @author Nick
 *
 */

@Entity
@Table(name="dgg_xzshituallocation")
public class XzShituAllocation {
	private Long id;
	private String jiedan;//接单状态
	private String qiyong;//是否启用
	private String depjiedan;//部门接单状态
	
	@Id 
	@Column(name = "id")
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "jiedan")
	public String getJiedan() {
		return jiedan;
	}
	public void setJiedan(String jiedan) {
		this.jiedan = jiedan;
	}
	@Column(name = "qiyong")
	public String getQiyong() {
		return qiyong;
	}
	public void setQiyong(String qiyong) {
		this.qiyong = qiyong;
	}
	@Column(name = "depjiedan")
	public String getDepjiedan() {
		return depjiedan;
	}
	public void setDepjiedan(String depjiedan) {
		this.depjiedan = depjiedan;
	}
}
