package com.wongs.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "tbl_address")
public class Address implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String ID_PREFIX = "ADD:";
	
	@Id
	@Column(name = "address_id", unique = true, nullable = false, length = 40)
	private String addressId;
	@Column(name = "line1", length = 100)
	private String line1;
	@Column(name = "line2", length = 100)
	private String line2;
	@Column(name = "line3", length = 100)
	private String line3;
	@Column(name = "line4", length = 100)
	private String line4;	
	@Column(name = "city", length = 100)
	private String city;
	@Column(name = "postal_code", length = 16)
	private String postalCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", nullable = true)
	private Country country;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id", nullable = true)
	private State state;

	Address() {
	}

	public Address(String addressId, String line1, String line2, String line3, String line4, String city,
			String postalCode, Country country, State state) {
		super();
		this.addressId = addressId;
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.line4 = line4;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.state = state;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	public String getLine4() {
		return line4;
	}

	public void setLine4(String line4) {
		this.line4 = line4;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
