package cn.dgg.CRM365.domain.authority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 公司管理(总公司，分公司) <功能简述> <功能详细描述>
 * 
 * @author chenqin
 * @version [版本号, Aug 9, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity
@Table(name = "dgg_department")
public class Department {

	private Long id;
	private String depaName;// 部门名称
	private String depaNotes;// 部门说明
	private String orderStatus;//接单状态（0为不签单、1为签单）
	private String remark;// 备注
	private Integer superId;
	private Department depa;
	
	public Department(Long id, String depaName, Integer superId) {
		this.id = id;
		this.depaName = depaName;
		this.superId = superId;
	}

	public Department() {
		
	}

	@Column(name = "superId")
	public Integer getSuperId() {
		return superId;
	}

	public void setSuperId(Integer superId) {
		this.superId = superId;
	}
	
	@ManyToOne
	@JoinColumn(name = "depa")
	public Department getDepa() {
		return depa;
	}

	public void setDepa(Department depa) {
		this.depa = depa;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "depaName")
	public String getDepaName() {
		return depaName;
	}

	public void setDepaName(String depaName) {
		this.depaName = depaName;
	}

	@Column(name = "depaNotes")
	public String getDepaNotes() {
		return depaNotes;
	}

	public void setDepaNotes(String depaNotes) {
		this.depaNotes = depaNotes;
	}

	@Column(name="orderStatus")
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
}
