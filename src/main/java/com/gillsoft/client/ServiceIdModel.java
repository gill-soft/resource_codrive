package com.gillsoft.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gillsoft.model.AbstractJsonModel;

@JsonInclude(Include.NON_NULL)
public class ServiceIdModel extends AbstractJsonModel {
	
	private static final long serialVersionUID = 7614884907411639503L;

	private String invoiceId;
	
	private String ticketNumber;

	public ServiceIdModel() {
		
	}

	public ServiceIdModel(String invoiceId, String ticketNumber) {
		this.setInvoiceId(invoiceId);
		this.setTicketNumber(ticketNumber);
	}

	@Override
	public ServiceIdModel create(String json) {
		return (ServiceIdModel) super.create(json);
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	
}
