package com.gillsoft.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "bus_soft" })
public class Seats implements Serializable {
	
	private static final long serialVersionUID = 9175402545417459172L;
	
	@JsonProperty("bus_soft")
	private BusSoft busSoft;

	@JsonProperty("bus_soft")
	public BusSoft getBusSoft() {
		return busSoft;
	}

	@JsonProperty("bus_soft")
	public void setBusSoft(BusSoft busSoft) {
		this.busSoft = busSoft;
	}

}
