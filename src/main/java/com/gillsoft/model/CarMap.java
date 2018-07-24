package com.gillsoft.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarMap {

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
}
