package com.gillsoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
	
	@JsonProperty("class")
	private String clas;
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("message")
	private String message;

	public String getClas() {
		return clas;
	}

	public void setClas(String clas) {
		this.clas = clas;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
