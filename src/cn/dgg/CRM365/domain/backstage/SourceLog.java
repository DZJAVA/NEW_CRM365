package cn.dgg.CRM365.domain.backstage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 客户信息管理
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  黄剑锋
  * @version  [版本号, Dec 19, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@Entity
@Table(name="loan_source_log")
public class SourceLog {
	private Integer id;//id
	private String logDate;
	private String logInfo;
	private int source;
	
	@Id
	@Column(name = "id")
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "logDate")
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	
	@Column(name = "logInfo")
	public String getLogInfo() {
		return logInfo;
	}
	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	
	@Column(name = "source")
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
}
