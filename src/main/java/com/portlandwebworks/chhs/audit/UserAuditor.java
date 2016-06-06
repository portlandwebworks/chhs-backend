package com.portlandwebworks.chhs.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class UserAuditor implements AuditorAware<String>{

	private final static Logger log = LoggerFactory.getLogger(UserAuditor.class);

	@Override
	public String getCurrentAuditor() {
		log.trace("Getting auditor name.");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth == null || isAnon(auth)){
			return "SYSTEM";
		}else{
			return null;
		}
	}

	private boolean isAnon(Authentication auth) {
		return auth.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_ANONYMOUS"))
				.findFirst().isPresent();
	}
	
}
