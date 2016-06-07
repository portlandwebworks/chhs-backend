package com.portlandwebworks.chhs.audit;

import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class UserAuditor implements AuditorAware<String>{

	private final static Logger log = LoggerFactory.getLogger(UserAuditor.class);
	
	private final AuthenticationDetailsProvider authProvider;

	@Autowired
	public UserAuditor(AuthenticationDetailsProvider authProvider) {
		this.authProvider = authProvider;
	}
	
	@Override
	public String getCurrentAuditor() {
		log.trace("Getting auditor name.");
		AuthenticationDetails auth = authProvider.authenticated();
		
		if(auth == null || isAnon(auth)){
			return "SYSTEM";
		}else{
			return auth.getUsername();
		}
	}

	private boolean isAnon(AuthenticationDetails auth) {
		return auth.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_ANONYMOUS"))
				.findFirst().isPresent();
	}
	
}
