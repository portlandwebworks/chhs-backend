package com.portlandwebworks.chhs.accounts.model;

import com.portlandwebworks.chhs.BasePersistable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nick
 */
@Entity
@Table(name="addresses")
public class Address extends BasePersistable{
	
	@NotNull
	private String city;
	@NotNull
	private String state;
	@NotNull
	private String postalCode;
	@NotNull
	private String country;
	@NotNull
	private String street;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
}
