package cn.dgg.CRM365.domain.authority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 角色栏目表（主要是管理角色与菜单的关系）
 * 
 * @author 王科(大) 2012-11-15
 */
@Entity
@Table(name = "dgg_menuoperationrole")
public class MenuOperationRole {

	private Long id;// 角色与菜单操作编号
	private String menuOperation;// 关联菜单操作
	private Role role;// 角色
	private String authorizedType;// 授权类型

	@Id
	@Column(name = "id")
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "menuOperation")
	public String getMenuOperation() {
		return menuOperation;
	}

	public void setMenuOperation(String menuOperation) {
		this.menuOperation = menuOperation;
	}

	@ManyToOne
	@JoinColumn(name = "role")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "authorizedType")
	public String getAuthorizedType() {
		return authorizedType;
	}

	public void setAuthorizedType(String authorizedType) {
		this.authorizedType = authorizedType;
	}
}