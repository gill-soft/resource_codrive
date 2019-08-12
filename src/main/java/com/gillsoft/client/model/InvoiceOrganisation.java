package com.gillsoft.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceOrganisation {
	
	@JsonProperty("text")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

