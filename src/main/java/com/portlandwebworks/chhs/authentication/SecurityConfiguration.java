package com.portlandwebworks.chhs.authentication;

import com.portlandwebworks.chhs.authentication.token.TokenAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

/**
 *
 * @author nick
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private TokenAuthFilter tokenAuthFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/api/swagger.json").permitAll()
				.antMatchers(HttpMethod.POST, "/api/token").anonymous()
				.antMatchers(HttpMethod.GET, "/api/account/available").anonymous()
				.antMatchers(HttpMethod.POST, "/api/account").anonymous()
				.antMatchers("/api/**").authenticated()
				.and()
				.addFilter(tokenAuthFilter)
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new Http403ForbiddenEntryPoint();
	}

}
