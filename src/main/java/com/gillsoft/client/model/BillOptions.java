package com.gillsoft.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillOptions implements Serializable{

	private static final long serialVersionUID = 655434238209329228L;

	@JsonProperty("owner_email")
	private String ownerEmail;
	
	@JsonProperty("owner_phone")
	private String ownerPhone;

	@JsonProperty("seller_price_total")
	private String sellerPriceTotal;
	
	public BillOptions() {}
	
	public BillOptions(String ownerEmail, String ownerPhone, String sellerPriceTotal) {
		this.ownerEmail = ownerEmail;
		this.ownerPhone = ownerPhone;
		this.sellerPriceTotal = sellerPriceTotal;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getOwnerPhone() {
		return ownerPhone;
	}

	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone = ownerPhone;
	}

	public String getSellerPriceTotal() {
		return sellerPriceTotal;
	}

	public void setSellerPriceTotal(String sellerPriceTotal) {
		this.sellerPriceTotal = sellerPriceTotal;
	}
	
}
