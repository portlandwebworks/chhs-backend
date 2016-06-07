package com.portlandwebworks.chhs.tokens;

import com.portlandwebworks.chhs.tokens.beans.PasswordChecker;
import com.portlandwebworks.chhs.tokens.beans.TokenGenerator;
import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

	private final static Logger log = LoggerFactory.getLogger(TokenResource.class);
	private final PasswordChecker passwordChecker;
	private final TokenGenerator generator;
	private final AuthenticationDetailsProvider authProvider;

	@Autowired
	public TokenResource(PasswordChecker passwordChecker, TokenGenerator generator, AuthenticationDetailsProvider authProvider) {
		this.passwordChecker = passwordChecker;
		this.generator = generator;
		this.authProvider = authProvider;
	}

	@ApiOperation(value = "Allows a user to attempt to generate and retrieve an authentication token.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Email and Password valid, new token generated and returned."),
		@ApiResponse(code = 403, message = "Email and/or Password are note valid.")
	})
	@POST
	@Transactional
	public AuthenticationDetails token(TokenRequest request) {
		log.info("User {} is attempting to generate a new token.", request.getEmail());
		assert (request.getEmail() != null);
		assert (request.getPassword() != null);
		if (passwordChecker.passwordMatches(request.getEmail(), request.getPassword())) {
			String token = generator.generateTokenFor(request.getEmail()).getToken();
			log.debug("User {} is successfully generated a token.", request.getEmail());
			return authProvider.forToken(token);
		} else {
			log.debug("User {} is failed to generate a token.", request.getEmail());
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
	}

	@ApiOperation(value = "Retrieve authentication details for a given token.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Authentication details if token is valid."),
		@ApiResponse(code = 403, message = "If token is not provided or is not valid")
	})
	@GET
	@Transactional
	public AuthenticationDetails authDetailsForToken(@HeaderParam("Token") String token) {
		log.debug("Getting user info for current token {}", token);
		try {
			if(authProvider.authenticated().getToken().equals(token)){
				return authProvider.forToken(token);
			}else{
				throw new WebApplicationException(Response.Status.FORBIDDEN);
			}
		} catch (NoResultException ex) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

}
