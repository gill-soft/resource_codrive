package com.gillsoft.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceArticle implements Serializable {

	private static final long serialVersionUID = 9121422377478936527L;

	@JsonProperty("code")
	private String code;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("price")
	private long price;

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

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}
	
}