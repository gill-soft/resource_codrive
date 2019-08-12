package com.gillsoft.client.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person implements Serializable {

	private static final long serialVersionUID = 693477322400294207L;
	private static final String PERSON_TYPE_ADULT = "adult";
	private static final String PERSON_TYPE_CHILD = "child";
	private static final String PERSON_TYPE_INFANT = "infant";

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("surname")
	private String surname;
	
	@JsonProperty("privilege")
	private String privilege;
	
	@JsonProperty("type")
	private String type = PERSON_TYPE_ADULT;
	
	@JsonProperty("birth_date")
	private Date birthDate;
	
	@JsonProperty("passport")
	private Passport passport;
	
	public Person() {}
	
	public Person(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}
	
	public Person(String name, String surname, String privilege, String type, Date birthDate, Passport passport) {
		this.name = name;
		this.surname = surname;
		this.privilege = privilege;
		this.type = type;
		this.birthDate = birthDate;
		this.passport = passport;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Passport getPassport() {
		return passport;
	}

	public void setPassport(Passport passport) {
		this.passport = passport;
	}
	
}
