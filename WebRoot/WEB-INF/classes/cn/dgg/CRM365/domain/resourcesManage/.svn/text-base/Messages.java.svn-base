package cn.dgg.CRM365.domain.resourcesManage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 短信记录
 * 
 * @author 王玉川
 * @version  [版本号, 2012,12,24]
 *
 */
@Entity
@Table(name="dgg_messages")
public class Messages {

	private Long id;
	// 发送人
	private String sender;
	// 接收人
	private String receivedBy;
	// 发送内容
	private String content;
	// 发送时间
	private String sendtime;
    @Id
    @Column(name="id")
    @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="sender")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	@Column(name="content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="sendtime")
	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	@Column(name="receivedBy")
	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}
 


}
