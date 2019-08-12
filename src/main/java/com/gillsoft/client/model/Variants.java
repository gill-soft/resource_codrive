package com.gillsoft.client.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Variants {

	@JsonProperty("ctx")
	private Ctx ctx;
	@JsonProperty("vars")
	private List<Var> vars = null;

	@JsonProperty("ctx")
	public Ctx getCtx() {
		return ctx;
	}

	@JsonProperty("ctx")
	public void setCtx(Ctx ctx) {
		this.ctx = ctx;
	}

	@JsonProperty("vars")
	public List<Var> getVars() {
		return vars;
	}

	@JsonProperty("vars")
	public void setVars(List<Var> vars) {
		this.vars = vars;
	}

}
