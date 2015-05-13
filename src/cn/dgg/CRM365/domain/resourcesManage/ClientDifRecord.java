package cn.dgg.CRM365.domain.resourcesManage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 记录自动分配时没有分配完的信息
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  黄剑锋
  * @version  [版本号, Dec 19, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@Entity
@Table(name="dgg_clientDifRecord")
public class ClientDifRecord {
	private int id;//id
	private int userid;//用户id
	private int clientDif;//客户资源差数
	private int flag;//备注
	
	@Id
	@Column(name = "id")
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "userid")
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	@Column(name = "clientDif")
	public int getClientDif() {
		return clientDif;
	}
	public void setClientDif(int clientDif) {
		this.clientDif = clientDif;
	}
	@Column(name = "flag")
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
