package com.portlandwebworks.chhs.messages.model;

import com.portlandwebworks.chhs.BasePersistable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * A fancy join table to keep track of what messages a user has marked as
 * deleted.
 *
 * @author nick
 */
@Entity
@Table(name = "messages_deleted")
public class DeletedMessage extends BasePersistable {

	@NotNull
	private Integer messageId;
	@NotNull
	private Integer forUserId;

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getForUserId() {
		return forUserId;
	}

	public void setForUserId(Integer forUserId) {
		this.forUserId = forUserId;
	}
	
}
