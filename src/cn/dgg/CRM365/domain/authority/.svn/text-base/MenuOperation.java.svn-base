package cn.dgg.CRM365.domain.authority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 菜单操作bean
 * 
 * @author c 2012-11-15 王科(大)
 */
@Entity
@Table(name = "dgg_menuoperation")
public class MenuOperation {
	private Long id;// 菜单操作编号
	private Menu menu_id;// 关联菜单
	private String operationName;// 操作名称
	private String displayOrder;// 显示顺序
	private String imageName;// 图片名称
	private String operationRemark;// 操作备注

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
	@JoinColumn(name = "menu_id")
	public Menu getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Menu menu_id) {
		this.menu_id = menu_id;
	}

	@Column(name = "operationName")
	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	@Column(name = "imageName")
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Column(name = "operationRemark")
	public String getOperationRemark() {
		return operationRemark;
	}

	public void setOperationRemark(String operationRemark) {
		this.operationRemark = operationRemark;
	}

	@Column(name = "displayOrder")
	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
}