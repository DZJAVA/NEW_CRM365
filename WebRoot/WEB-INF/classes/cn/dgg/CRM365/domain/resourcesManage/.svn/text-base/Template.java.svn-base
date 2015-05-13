package cn.dgg.CRM365.domain.resourcesManage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模板管理
 * 
 * @author 王玉川
 * @version  [版本号, 2012,12,25]
 *
 */
@Entity
@Table(name="dgg_template")
public class Template {

	private Long id;
	// 模板标题
	private String titles;
	// 内容
	private String content;
	// 定时发送时间
	private String timingTime;
	// 发送状态,1未发送,2已发送
	private String sendState;
    @Id
    @Column(name="id")
    @GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    @Column(name="titles")
	public String getTitles() {
		return titles;
	}
	public void setTitles(String titles) {
		this.titles = titles;
	}
    @Column(name="content")
	public String getContent() {
		return content;
	}
    
	public void setContent(String content) {
		this.content = content;
	}
    @Column(name="timingTime")
	public String getTimingTime() {
		return timingTime;
	}
	public void setTimingTime(String timingTime) {
		this.timingTime = timingTime;
	}
    @Column(name="sendState")
	public String getSendState() {
		return sendState;
	}
	public void setSendState(String sendState) {
		this.sendState = sendState;
	}
}
