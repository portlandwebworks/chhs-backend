package com.portlandwebworks.chhs.messages;

import com.portlandwebworks.chhs.messages.model.Message;

/**
 *
 * @author nick
 */
public class MessageCreatedEvent {
	
	private Integer msgId;
	
	public MessageCreatedEvent(Message msg) {
		this.msgId = msg.getId();
	}

	public Integer getMsgId() {
		return msgId;
	}
	
}
