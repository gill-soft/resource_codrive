package com.gillsoft.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoicePrice implements Serializable {

	private static final long serialVersionUID = -7582634045950793918L;

	@JsonProperty("total")
	private Long total;
	
	@JsonProperty("tax")
	private Long tax;
	
	@JsonProperty("articles")
	private List<InvoiceArticle> articles = new ArrayList<>();

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getTax() {
		return tax;
	}

	public void setTax(Long tax) {
		this.tax = tax;
	}

	public List<InvoiceArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<InvoiceArticle> articles) {
		this.articles = articles;
	}
	
}