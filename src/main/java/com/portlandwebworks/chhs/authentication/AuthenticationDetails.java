package com.portlandwebworks.chhs.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author nick
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationDetails implements UserDetails {
	
	private Integer accountId;
	private String email;
	private String token;
	private List<String> roles;

	public AuthenticationDetails(Integer accountId, String email, String token, List<String> roles) {
		this.accountId = accountId;
		this.email = email;
		this.token = token;
		this.roles = roles;
	}

	public Integer getAccountId() {
		return accountId;
	}
	
	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getToken() {
		return token;
	}

	public List<String> getRoles() {
		return roles;
	}
	
}
