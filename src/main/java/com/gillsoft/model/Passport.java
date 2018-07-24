package com.gillsoft.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Passport {

	@JsonProperty("number")
	private String number;
	
	@JsonProperty("citizenship")
	private String citizenship;
	
	@JsonProperty("end_date")
	private Date endDate;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}

