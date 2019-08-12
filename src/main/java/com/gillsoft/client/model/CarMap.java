package com.gillsoft.client.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarMap implements Serializable {

	private static final long serialVersionUID = -5401632619559302011L;

	private static final ParameterizedTypeReference<CarMap> typeRef = new ParameterizedTypeReference<CarMap>() { };

	@JsonProperty("free_places")
	private List<String> freePlaces;
	
	@JsonProperty("busy_places")
	private List<String> busyPlaces;

	@JsonProperty("free_places")
	public List<String> getFreePlaces() {
		return freePlaces;
	}

	@JsonProperty("free_places")
	public void setFreePlaces(List<String> freePlaces) {
		this.freePlaces = freePlaces;
	}
	
	@JsonProperty("busy_places")
	public List<String> getBusyPlaces() {
		return busyPlaces;
	}

	@JsonProperty("busy_places")
	public void setBusyPlaces(List<String> busyPlaces) {
		this.busyPlaces = busyPlaces;
	}

	public static ParameterizedTypeReference<CarMap> getTypeReference() {
		return typeRef;
	}
}
