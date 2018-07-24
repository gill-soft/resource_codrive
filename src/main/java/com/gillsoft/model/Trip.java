package com.gillsoft.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "src", "dst", "transporter", "state", "hardware", "el", "max_tickets_per_invoice" })
public class Trip implements Serializable {

	private static final long serialVersionUID = 2448766572879330638L;
	
	private static final String SALE_LOCKED = "101"; //продажа заблокирована
	private static final String TRIP_LOCKED = "1"; //рейс заблокирован
	private static final String TRIP_CANCELED = "2"; //рейс сорван
	private static final String TRIP_GONE = "3"; //рейс уехал с данной станции

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("src")
	private Point src;
	@JsonProperty("dst")
	private Point dst;
	@JsonProperty("transporter")
	private String transporter;
	@JsonProperty("state")
	private String state;
	@JsonProperty("hardware")
	private String hardware;
	@JsonProperty("el")
	private String el;
	@JsonProperty("max_tickets_per_invoice")
	private Integer maxTicketsPerInvoice;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("src")
	public Point getSrc() {
		return src;
	}

	@JsonProperty("src")
	public void setSrc(Point src) {
		this.src = src;
	}

	@JsonProperty("dst")
	public Point getDst() {
		return dst;
	}

	@JsonProperty("dst")
	public void setDst(Point dst) {
		this.dst = dst;
	}

	@JsonProperty("transporter")
	public String getTransporter() {
		return transporter;
	}

	@JsonProperty("transporter")
	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	@JsonProperty("state")
	public String getState() {
		return state;
	}

	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("hardware")
	public String getHardware() {
		return hardware;
	}

	@JsonProperty("hardware")
	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	@JsonProperty("el")
	public String getEl() {
		return el;
	}

	@JsonProperty("el")
	public void setEl(String el) {
		this.el = el;
	}

	@JsonProperty("max_tickets_per_invoice")
	public Integer getMaxTicketsPerInvoice() {
		return maxTicketsPerInvoice;
	}

	@JsonProperty("max_tickets_per_invoice")
	public void setMaxTicketsPerInvoice(Integer maxTicketsPerInvoice) {
		this.maxTicketsPerInvoice = maxTicketsPerInvoice;
	}

	public boolean isNotForSale() {
		return SALE_LOCKED.equals(state) || TRIP_CANCELED.equals(state) || TRIP_LOCKED.equals(state)
				|| TRIP_GONE.equals(state);
	}

}