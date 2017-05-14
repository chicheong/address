package com.wongs.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "tbl_country")
public class Country implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "country_id", unique = true, nullable = false, length = 2)
	private String countryId;
	@Column(name = "code", length = 3)
	private String code;
	@Column(name = "num", length = 3)
	private String num;
	@Column(name = "name", length = 100)
	private String name;

	Country() {
	}
	
	public Country(String countryId, String code, String num, String name) {
		super();
		this.countryId = countryId;
		this.code = code;
		this.num = num;
		this.name = name;
	}

	public Country(String countryId) {
		this.countryId = countryId;
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

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
