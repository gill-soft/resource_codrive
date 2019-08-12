package com.gillsoft.client.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "src", "dst", "dep_date", "ret_date", "other_src", "other_dst", "used_src", "used_dst" })
public class Ctx implements Serializable {

	private static final long serialVersionUID = 5717534382758478070L;

	@JsonProperty("src")
	private Point src;
	@JsonProperty("dst")
	private Point dst;
	@JsonProperty("dep_date")
	private String depDate;
	@JsonProperty("ret_date")
	private String retDate;
	@JsonProperty("other_src")
	private List<Point> otherSrc = null;
	@JsonProperty("other_dst")
	private List<Point> otherDst = null;
	@JsonProperty("used_src")
	private Integer usedSrc;
	@JsonProperty("used_dst")
	private Integer usedDst;

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

	@JsonProperty("dep_date")
	public String getDepDate() {
		return depDate;
	}

	@JsonProperty("dep_date")
	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}

	@JsonProperty("ret_date")
	public String getRetDate() {
		return retDate;
	}

	@JsonProperty("ret_date")
	public void setRetDate(String retDate) {
		this.retDate = retDate;
	}

	@JsonProperty("other_src")
	public List<Point> getOtherSrc() {
		return otherSrc;
	}

	@JsonProperty("other_src")
	public void setOtherSrc(List<Point> otherSrc) {
		this.otherSrc = otherSrc;
	}

	@JsonProperty("other_dst")
	public List<Point> getOtherDst() {
		return otherDst;
	}

	@JsonProperty("other_dst")
	public void setOtherDst(List<Point> otherDst) {
		this.otherDst = otherDst;
	}

	@JsonProperty("used_src")
	public Integer getUsedSrc() {
		return usedSrc;
	}

	@JsonProperty("used_src")
	public void setUsedSrc(Integer usedSrc) {
		this.usedSrc = usedSrc;
	}

	@JsonProperty("used_dst")
	public Integer getUsedDst() {
		return usedDst;
	}

	@JsonProperty("used_dst")
	public void setUsedDst(Integer usedDst) {
		this.usedDst = usedDst;
	}

}