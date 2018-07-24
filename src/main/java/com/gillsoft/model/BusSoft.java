package com.gillsoft.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "free", "price", "fee", "tos" })
public class BusSoft implements Serializable {

	private static final long serialVersionUID = 2859389772802951913L;
	
	@JsonProperty("free")
	private Integer free;
	@JsonProperty("price")
	private Integer price;
	@JsonProperty("fee")
	private Integer fee;
	@JsonProperty("tos")
	private Integer tos;

	@JsonProperty("free")
	public Integer getFree() {
		return free;
	}

	@JsonProperty("free")
	public void setFree(Integer free) {
		this.free = free;
	}

	@JsonProperty("price")
	public Integer getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(Integer price) {
		this.price = price;
	}

	@JsonProperty("fee")
	public Integer getFee() {
		return fee;
	}

	@JsonProperty("fee")
	public void setFee(Integer fee) {
		this.fee = fee;
	}

	@JsonProperty("tos")
	public Integer getTos() {
		return tos;
	}

	@JsonProperty("tos")
	public void setTos(Integer tos) {
		this.tos = tos;
	}

}