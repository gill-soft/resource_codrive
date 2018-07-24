package com.gillsoft.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillSeat implements Serializable {

	private static final long serialVersionUID = 7910106007727194265L;

	@JsonProperty("person")
	private Person person;

	public BillSeat() {}
	
	public BillSeat(String name, String surname) {
		this.person = new Person(name, surname);
	}

	public BillSeat(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
