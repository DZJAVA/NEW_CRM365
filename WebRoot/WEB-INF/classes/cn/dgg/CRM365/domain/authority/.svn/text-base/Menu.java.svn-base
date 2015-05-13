package cn.dgg.CRM365.domain.authority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 菜单栏目
 * 
 * @author chenqin
 * 
 */

@Entity
@Table(name = "dgg_menu")
public class Menu {

	private Long id;
	private Menu systemMenu;// 上级菜单
	private String resourceURL;// 访问资源路径
	private Integer smenIndex;// 排列顺序
	private String smenCaption;// 标题
	private String smenIcon;// 图标路径
	private String smenHint;// 提示

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
	@JoinColumn(name = "systemMenu")
	public Menu getSystemMenu() {
		return systemMenu;
	}

	public void setSystemMenu(Menu systemMenu) {
		this.systemMenu = systemMenu;
	}

	@Column(name = "resourceURL")
	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	@Column(name = "smenIndex")
	public Integer getSmenIndex() {
		return smenIndex;
	}

	public void setSmenIndex(Integer smenIndex) {
		this.smenIndex = smenIndex;
	}

	@Column(name = "smenCaption")
	public String getSmenCaption() {
		return smenCaption;
	}

	public void setSmenCaption(String smenCaption) {
		this.smenCaption = smenCaption;
	}

	@Column(name = "smenIcon")
	public String getSmenIcon() {
		return smenIcon;
	}

	public void setSmenIcon(String smenIcon) {
		this.smenIcon = smenIcon;
	}

	@Column(name = "smenHint")
	public String getSmenHint() {
		return smenHint;
	}

	public void setSmenHint(String smenHint) {
		this.smenHint = smenHint;
	}
}
