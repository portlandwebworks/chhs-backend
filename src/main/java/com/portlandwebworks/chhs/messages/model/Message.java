package com.portlandwebworks.chhs.messages.model;

import com.portlandwebworks.chhs.BasePersistable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author nick
 */
@Entity
@Table(name = "messages")
public class Message extends BasePersistable {

	private String subject;
	private String content;
	private Integer replyToId;
	private Integer toUserId;
	private Integer fromUserId;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getReplyToId() {
		return replyToId;
	}

	public void setReplyToId(Integer replyToId) {
		this.replyToId = replyToId;
	}

	public Integer getToUserId() {
		return toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

}
