package com.portlandwebworks.chhs.accounts;

import com.portlandwebworks.chhs.accounts.beans.CaseWorkerFinder;
import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
@Scope("request")
@Path("/account/caseworker")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON, value = "Account Case Worker Resource")
public class CaseWorkerResource {
	
	
	private final CaseWorkerFinder caseWorkerFinder;

	@Autowired
	public CaseWorkerResource(CaseWorkerFinder caseWorkerFinder) {
		this.caseWorkerFinder = caseWorkerFinder;
	}
	
	@GET
	@ApiOperation("Finds the case worker for the currently authenticated account.")
	@ApiResponses(@ApiResponse(code = 200, message = "Case worker assigned to the currently authenticated account."))
	public Account currentCaseworker(){
		return caseWorkerFinder.findCaseWorker();
	}
	
}
