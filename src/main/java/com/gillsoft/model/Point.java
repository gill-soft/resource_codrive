package com.gillsoft.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({ "idx", "name", "latitude", "longitude", "country" })
public class Point implements Serializable {

	private static final long serialVersionUID = -5321084591692047898L;

	@JsonProperty("idx")
	private BigDecimal idx;
	@JsonProperty("name")
	private String name;
	@JsonProperty("latitude")
	private BigDecimal latitude;
	@JsonProperty("longitude")
	private BigDecimal longitude;
	@JsonProperty("country")
	private String country;

	@JsonProperty("idx")
	public BigDecimal getIdx() {
		return idx;
	}

	@JsonProperty("idx")
	public void setIdx(BigDecimal idx) {
		this.idx = idx;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("latitude")
	public BigDecimal getLatitude() {
		return latitude;
	}

	@JsonProperty("latitude")
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	@JsonProperty("longitude")
	public BigDecimal getLongitude() {
		return longitude;
	}

	@JsonProperty("longitude")
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}

}
