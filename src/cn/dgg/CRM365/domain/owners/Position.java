package cn.dgg.CRM365.domain.owners;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 职位管理
 * 
 * @author 王玉川
 * @version[版本号，2012,12,17]
 */
@Entity
@Table(name = "dgg_position")
public class Position {
	private Long id;
	private String name;// 公司名称
	private String remark;// 备注

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
