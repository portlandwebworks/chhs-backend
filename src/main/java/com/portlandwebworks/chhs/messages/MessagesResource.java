package com.portlandwebworks.chhs.messages;

import com.portlandwebworks.chhs.messages.beans.MessageFinder;
import com.portlandwebworks.chhs.messages.beans.MessageSaver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nick
 */
@Component
@Scope("request")
@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON, value = "Messages Resource")
public class MessagesResource {
	
	private final MessageSaver saver;
	private final MessageFinder finder;

	@Autowired
	public MessagesResource(MessageSaver saver, MessageFinder finder) {
		this.saver = saver;
		this.finder = finder;
	}
	
	@ApiOperation("Post a new message, from items automatically filled in with authenticated user details.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Message successfully posted and saved.")
	})
	@POST
	@Transactional
	public Response postMessage(MessageDTO newMessage){
		return Response.ok(saver.saveMessage(newMessage)).build();
	}
	
	
	@ApiOperation("Allows user to retrieve all messages that are either from them or to them, sorted by create order.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "List of messages for authenticated user.")
	})
	@GET
	@Transactional
	public List<MessageDTO> getMessages(){
		return finder.getMessages();
	}
	
}
