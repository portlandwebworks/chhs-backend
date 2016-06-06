package com.portlandwebworks.chhs.tokens;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Api(value = "Generate and validate authentication tokens.")
@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TokenResource {

	private Logger log = LoggerFactory.getLogger(TokenResource.class);
	private final PasswordChecker passwordChecker;
	private final TokenGenerator generator;

	@Autowired
	public TokenResource(PasswordChecker passwordChecker, TokenGenerator generator) {
		this.passwordChecker = passwordChecker;
		this.generator = generator;
	}

	@ApiOperation(value = "Allows a user to attempt to generate and retrieve an authentication token.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Email and Password valid, new token generated and returned."),
		@ApiResponse(code = 400, message = "Email and/or Password are note valid.")
	})
	@POST
	@Transactional
	public String token(TokenRequest request) {
		assert (request.getEmail() != null);
		assert (request.getPassword() != null);
		if (passwordChecker.passwordMatches(request.getEmail(), request.getPassword())) {
			return "\"" + generator.generateTokenFor(request.getEmail()).getToken() + "\"";
		} else {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

}
