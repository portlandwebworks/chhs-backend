package com.portlandwebworks.chhs.jersey;

import com.portlandwebworks.chhs.accounts.AccountResource;
import com.portlandwebworks.chhs.accounts.CaseWorkerResource;
import com.portlandwebworks.chhs.messages.MessagesResource;
import com.portlandwebworks.chhs.tokens.TokenResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
@ApplicationPath(JerseyConfig.API_PATH)
public class JerseyConfig extends ResourceConfig {

	public static final String API_PATH = "/api";
	
	public JerseyConfig() {
		this.register(AccountResource.class);
		this.register(TokenResource.class);
		this.register(CaseWorkerResource.class);
		this.register(MessagesResource.class);
		configureSwagger();
	}

	private final void configureSwagger() {
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);

		BeanConfig config = new BeanConfig();
		config.setConfigId("CHHS API");
		config.setTitle("CHHS Application API");
		config.setVersion("v1");
		config.setContact("Portland WebWorks");
		config.setSchemes(new String[]{"http", "https"});
		config.setBasePath(API_PATH);
		config.setResourcePackage("com.portlandwebworks.chhs");
		config.setPrettyPrint(true);
		config.setScan(true);
	}

}
