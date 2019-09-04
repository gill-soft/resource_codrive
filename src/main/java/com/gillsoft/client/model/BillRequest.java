package com.gillsoft.client.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillRequest {
	
	@JsonProperty("seats")
	private List<BillSeat> seats = new ArrayList<>();
	
	@JsonProperty("options")
	private BillOptions options;
	
	public BillRequest() {}
	
	public BillRequest(BillSeat seat, BillOptions options) {
		this.seats = Arrays.asList(seat);
		this.options = options;
	}
	
	public BillRequest(List<BillSeat> seats, BillOptions options) {
		this.seats = seats;
		this.options = options;
	}

	public List<BillSeat> getSeats() {
		return seats;
	}

	public void setSeats(List<BillSeat> seats) {
		this.seats = seats;
	}

	public BillOptions getOptions() {
		return options;
	}

	public void setOptions(BillOptions options) {
		this.options = options;
	}

}
