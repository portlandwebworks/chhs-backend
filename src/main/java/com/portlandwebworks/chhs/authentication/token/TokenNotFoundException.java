/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portlandwebworks.chhs.authentication.token;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author nick
 */
public class TokenNotFoundException extends AuthenticationException {

	public TokenNotFoundException(String msg) {
		super(msg);
	}
	
	public TokenNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

}
