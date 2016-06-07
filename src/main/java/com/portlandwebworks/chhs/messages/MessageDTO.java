package com.portlandwebworks.chhs.messages;

import io.swagger.annotations.ApiModel;
import java.util.Date;

/**
 * Map to a friendlier version of Message for easier consumption on the UI
 * side of things.
 * 
 * @author nick
 */
@ApiModel(value = "Message")
public class MessageDTO {
	
	private Integer id;
	private String fromName;
	private String fromEmail;
	private String toName;
	private String toEmail;
	private String subject;
	private String content;
	private Date createdAt;
	private Integer inReplyToId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getInReplyToId() {
		return inReplyToId;
	}

	public void setInReplyToId(Integer inReplyToId) {
		this.inReplyToId = inReplyToId;
	}
	
}
