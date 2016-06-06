package com.portlandwebworks.chhs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author nick
 */
@SpringBootApplication
@EnableWebSecurity
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "userAuditor")
public class BackendApp {

	private final int ENCRYPT_STRENGTH = 10;

	public static void main(String... args) {
		SpringApplication app = new SpringApplication(BackendApp.class);
		app.run(args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(ENCRYPT_STRENGTH);
	}

}
