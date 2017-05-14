package com.wongs.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "tbl_state")
public class State implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "state_id", unique = true, nullable = false, length = 2)
	private String stateId;
	@Column(name = "country_id", nullable= false, length = 2)
	private String countryId;
	@Column(name = "code", length = 3)
	private String code;
	@Column(name = "name", length = 100)
	private String name;

	State() {
	}

	public State(String stateId, String countryId, String code, String name) {
		super();
		this.stateId = stateId;
		this.countryId = countryId;
		this.code = code;
		this.name = name;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
