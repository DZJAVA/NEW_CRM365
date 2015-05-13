package cn.dgg.CRM365.domain.owners;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 细数
 * 
 * @author 王玉川
 * @version [版本号, 2012,12,19]
 */
@Entity
@Table(name = "dgg_counts")
public class Counts {

	private Long id;
	// 细数名称
	private String c_name;
	// 细数值
	private String c_value;
	// 备注
	private String c_remark;
    @Id
    @Column(name="id")
    @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    @Column(name="c_name")
	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
    @Column(name="c_value")
	public String getC_value() {
		return c_value;
	}

	public void setC_value(String c_value) {
		this.c_value = c_value;
	}
    @Column(name="c_remark")
	public String getC_remark() {
		return c_remark;
	}

	public void setC_remark(String c_remark) {
		this.c_remark = c_remark;
	}

}
