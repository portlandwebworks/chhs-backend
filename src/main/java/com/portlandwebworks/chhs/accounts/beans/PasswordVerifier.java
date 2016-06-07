package com.portlandwebworks.chhs.accounts.beans;

import java.util.Objects;
import java.util.regex.Pattern;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author nick
 */
@Component
public class PasswordVerifier {

	private final Pattern pattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{10,}");
	private final Logger log = LoggerFactory.getLogger(PasswordVerifier.class);
	static final String REQUIRED_MSG = "Password is required.";
	static final String DO_NOT_MATCH_MSG = "Passwords do not match.";
	static final String BAD_FORMAT_MSG = "Password must contain the following: [A-Z][a-z][0-9] and be at least 10 characters long.";

	/**
	 * Password must be not null, at least 10 characters and include one of
	 * each: [A-Z][a-z][0-9]
	 *
	 * @return true if password matches above rules.
	 * @throws ValidationException
	 */
	public boolean valid(String password, String passwordConfirmation) throws ValidationException {
		log.debug("Attempting to validate password.");
		if (StringUtils.isEmpty(password)) {
			log.debug("Password is empty.");
			throw new ValidationException(REQUIRED_MSG);
		} else if (!Objects.equals(password, passwordConfirmation)) {
			log.debug("Passwords do not match.");
			throw new ValidationException(DO_NOT_MATCH_MSG);
		} else if (!pattern.matcher(password).matches()) {
			log.debug("Password does not follow specified rules.");
			throw new ValidationException(BAD_FORMAT_MSG);
		}
		log.debug("Password valid.");
		return true;
	}

}
