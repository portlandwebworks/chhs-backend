package com.portlandwebworks.chhs.tokens;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nick
 */
public class TokenRequest {
	
	@NotNull
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
