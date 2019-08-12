package com.gillsoft.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gillsoft.client.model.Trip;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "guididx", "transport_type", "provider_id", "dep_date", "arr_date", "src_dep", "dst_arr",
		"duration", "trip", "seats" })
public class Var implements Serializable {

	private static final long serialVersionUID = -9041017630947963076L;

	@JsonProperty("guididx")
	private String guididx;
	@JsonProperty("transport_type")
	private String transportType;
	@JsonProperty("provider_id")
	private String providerId;
	@JsonProperty("dep_date")
	private String depDate;
	@JsonProperty("arr_date")
	private String arrDate;
	@JsonProperty("src_dep")
	private String srcDep;
	@JsonProperty("dst_arr")
	private String dstArr;
	@JsonProperty("duration")
	private String duration;
	@JsonProperty("trip")
	private Trip trip;
	@JsonProperty("seats")
	private Seats seats;

	@JsonProperty("guididx")
	public String getGuididx() {
		return guididx;
	}

	@JsonProperty("guididx")
	public void setGuididx(String guididx) {
		this.guididx = guididx;
	}

	@JsonProperty("transport_type")
	public String getTransportType() {
		return transportType;
	}

	@JsonProperty("transport_type")
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	@JsonProperty("provider_id")
	public String getProviderId() {
		return providerId;
	}

	@JsonProperty("provider_id")
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	@JsonProperty("dep_date")
	public String getDepDate() {
		return depDate;
	}

	@JsonProperty("dep_date")
	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}

	@JsonProperty("arr_date")
	public String getArrDate() {
		return arrDate;
	}

	@JsonProperty("arr_date")
	public void setArrDate(String arrDate) {
		this.arrDate = arrDate;
	}

	@JsonProperty("src_dep")
	public String getSrcDep() {
		return srcDep;
	}

	@JsonProperty("src_dep")
	public void setSrcDep(String srcDep) {
		this.srcDep = srcDep;
	}

	@JsonProperty("dst_arr")
	public String getDstArr() {
		return dstArr;
	}

	@JsonProperty("dst_arr")
	public void setDstArr(String dstArr) {
		this.dstArr = dstArr;
	}

	@JsonProperty("duration")
	public String getDuration() {
		return duration;
	}

	@JsonProperty("duration")
	public void setDuration(String duration) {
		this.duration = duration;
	}

	@JsonProperty("trip")
	public Trip getTrip() {
		return trip;
	}

	@JsonProperty("trip")
	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	@JsonProperty("seats")
	public Seats getSeats() {
		return seats;
	}

	@JsonProperty("seats")
	public void setSeats(Seats seats) {
		this.seats = seats;
	}

}