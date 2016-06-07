package com.portlandwebworks.chhs.tokens.model;

import com.portlandwebworks.chhs.BasePersistable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nick
 */
@Entity
@Table(name = "authentication_tokens")
@NamedQueries({
	@NamedQuery(name = Token.Q_FIND_BY_TOKEN, query = "SELECT t FROM Token t WHERE t.token = :token")
})
public class Token extends BasePersistable {

	public static final String Q_FIND_BY_TOKEN = "Token.findByToken";

	@NotNull
	private String token;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiresAt;

	@NotNull
	private Integer accountId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

}
