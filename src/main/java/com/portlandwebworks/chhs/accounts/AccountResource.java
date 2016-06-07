package com.portlandwebworks.chhs.accounts;

import com.portlandwebworks.chhs.accounts.beans.AccountCreator;
import com.portlandwebworks.chhs.accounts.beans.AccountUpdater;
import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.persistence.PersistenceException;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
@Path("/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON, value = "Account Resource")
public class AccountResource {

	private final static Logger log = LoggerFactory.getLogger(AccountResource.class);
	private final AccountCreator accountCreator;
	private final AccountUpdater accountUpdater;
	private final AuthenticationDetailsProvider provider;

	@Autowired
	public AccountResource(AccountCreator accountCreator, AccountUpdater accountUpdater, AuthenticationDetailsProvider provider) {
		this.accountCreator = accountCreator;
		this.accountUpdater = accountUpdater;
		this.provider = provider;
	}

	@ApiOperation(value = "Gets the current account for the logged in user based on the given token value.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Current user able to be retrieved"),
		@ApiResponse(code = 403, message = "Token invalid or not supplied.")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Token", paramType = "header", dataType = "string", required = true)
	})
	@GET
	public Account accountInfo() {
		return provider.currentAccount();
	}
	
	@ApiOperation(value = "Validates if an email is available for use.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "If email address is available for use."),
		@ApiResponse(code = 409, message = "If email address is already used..")
	})
	@GET
	@Path("/available")
	public Response availableEmail(@ApiParam(name = "email") @QueryParam("email") String email){
		if(accountCreator.emailAvailable(email)){
			return Response.ok().build();
		}else{
			return Response.status(Response.Status.CONFLICT).build();
		}
	}

	@ApiOperation("Create a new account.")
	@ApiResponses({
		@ApiResponse(code = 201, message = "User successfully registered."),
		@ApiResponse(code = 400, message = "Request failed validation and user could not be registered.")
	})
	@POST
	@Transactional
	public Response registerAccount(@NotNull @ApiParam(required = true) Account account) {
		try {
			accountCreator.createAccount(account);
			return Response.status(Response.Status.CREATED).build();
		} catch (PersistenceException ex) {
			log.error("Error persisting account information.", ex);
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		} catch (ValidationException ex) {
			log.error("Error validating account.", ex);
			//TODO Give more authenticated in the error response.
			throw new WebApplicationException(ex, Response.Status.BAD_REQUEST);
		} catch (Exception ex) {
			log.error("Error creating account.", ex);
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation("Update account. User must be logged in, and only allowed to edit their own account. Only the "
			+ "following properties are updatable; Account.firstName, Account.lastName, Account.Address.city,"
			+ " Account.Address.state, Account.Address.country, Account.Address.street, Account.Address.postalCode.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "User information successfully updated."),
		@ApiResponse(code = 400, message = "Update failed due to user input."),
		@ApiResponse(code = 500, message = "Update failed due to server error.")
	})
	@PUT
	@Transactional
	public Response updateAccount(@NotNull @ApiParam(required = true) Account account) {
		try {
			accountUpdater.updateAccount(account);
			return Response.status(Response.Status.OK).build();
		} catch (PersistenceException ex) {
			log.error("Error persisting account information.", ex);
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		} catch (ValidationException ex) {
			log.error("Error validating account.", ex);
			//TODO Give more authenticated in the error response.
			throw new WebApplicationException(ex, Response.Status.BAD_REQUEST);
		} catch (Exception ex) {
			log.error("Error creating account.", ex);
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
}
