package com.gillsoft.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Stations implements Serializable {

	private static final long serialVersionUID = -7928100307549422684L;

	@JsonProperty("stations")
	private List<Point> stations;

	@JsonProperty("stations")
	public List<Point> getStations() {
		return stations;
	}

	@JsonProperty("stations")
	public void setstations(List<Point> stations) {
		this.stations = stations;
	}

}
