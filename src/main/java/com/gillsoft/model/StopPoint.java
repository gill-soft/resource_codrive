package com.gillsoft.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "idx", "code", "guididx", "name", "latitude", "longitude", "transport_type", "country" })
public class StopPoint extends Point {

	@JsonProperty("code")
	private BigDecimal code;
	@JsonProperty("guididx")
	private String guididx;
	@JsonProperty("transport_type")
	private BigDecimal transportType;

	@JsonProperty("guididx")
	public String getGuididx() {
		return guididx;
	}

	@JsonProperty("guididx")
	public void setGuididx(String guididx) {
		this.guididx = guididx;
	}

	@JsonProperty("transport_type")
	public BigDecimal getTransportType() {
		return transportType;
	}

	@JsonProperty("transport_type")
	public void setTransportType(BigDecimal transportType) {
		this.transportType = transportType;
	}

}
