package com.gillsoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Counterparts {
	
	@JsonProperty("transporter")
	private InvoiceOrganisation transporter;
	
	@JsonProperty("insurer")
	private InvoiceOrganisation insurer;

	public InvoiceOrganisation getTransporter() {
		return transporter;
	}

	public void setTransporter(InvoiceOrganisation transporter) {
		this.transporter = transporter;
	}

	public InvoiceOrganisation getInsurer() {
		return insurer;
	}

	public void setInsurer(InvoiceOrganisation insurer) {
		this.insurer = insurer;
	}

}

