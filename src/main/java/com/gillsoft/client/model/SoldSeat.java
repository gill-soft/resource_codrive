package com.gillsoft.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SoldSeat {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("passenger")
	private Person passenger;
	
	@JsonProperty("price")
	private InvoicePrice price;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Person getPassenger() {
		return passenger;
	}

	public void setPassenger(Person passenger) {
		this.passenger = passenger;
	}

	public InvoicePrice getPrice() {
		return price;
	}

	public void setPrice(InvoicePrice price) {
		this.price = price;
	}
	
}
