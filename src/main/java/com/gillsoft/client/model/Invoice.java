package com.gillsoft.client.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gillsoft.LocalityServiceController;
import com.gillsoft.model.CalcType;
import com.gillsoft.model.Commission;
import com.gillsoft.model.Currency;
import com.gillsoft.model.Lang;
import com.gillsoft.model.Organisation;
import com.gillsoft.model.Price;
import com.gillsoft.model.Seat;
import com.gillsoft.model.Segment;
import com.gillsoft.model.Tariff;
import com.gillsoft.model.ValueType;
import com.gillsoft.util.Date;
import com.gillsoft.util.StringUtil;

public class Invoice {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("sale_point_name")
	private String salePointName;
	
	@JsonProperty("asps_code")
	private String aspsCode;
	
	@JsonProperty("asps_code_2")
	private String aspsCode2;
	
	@JsonProperty("asps_code_3")
	private String aspsCode3;
	
	@JsonProperty("state")
	private Integer state;
	
	@JsonProperty("creation_time")
	private String creationTime;
	
	@JsonProperty("owner_email")
	private String ownerEmail;
	
	@JsonProperty("owner_phone")
	private String ownerPhone;
	
	@JsonProperty("travel")
	private Var travel;
	
	@JsonProperty("sold_seats")
	private List<SoldSeat> soldSeats = new ArrayList<>();
	
	@JsonProperty("counterparts")
	private Counterparts counterparts;
	
	@JsonProperty("error")
	private Error error;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSalePointName() {
		return salePointName;
	}

	public void setSalePointName(String salePointName) {
		this.salePointName = salePointName;
	}

	public String getAspsCode() {
		return aspsCode;
	}

	public void setAspsCode(String aspsCode) {
		this.aspsCode = aspsCode;
	}

	public String getAspsCode2() {
		return aspsCode2;
	}

	public void setAspsCode2(String aspsCode2) {
		this.aspsCode2 = aspsCode2;
	}

	public String getAspsCode3() {
		return aspsCode3;
	}

	public void setAspsCode3(String aspsCode3) {
		this.aspsCode3 = aspsCode3;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
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

	public Var getTravel() {
		return travel;
	}

	public void setTravel(Var travel) {
		this.travel = travel;
	}

	public List<SoldSeat> getSoldSeats() {
		return soldSeats;
	}

	public void setSoldSeats(List<SoldSeat> soldSeats) {
		this.soldSeats = soldSeats;
	}

	public Counterparts getCounterparts() {
		return counterparts;
	}

	public void setCounterparts(Counterparts counterparts) {
		this.counterparts = counterparts;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
	
	@JsonIgnore
	public Segment getSegment() {
		Segment segment = new Segment();
		segment.setId(travel.getGuididx());
		segment.setNumber(travel.getTrip().getId());
		segment.setDepartureDate(Date.getFullDateString(travel.getDepDate(), travel.getSrcDep()));
		segment.setArrivalDate(Date.getFullDateString(travel.getArrDate(), travel.getDstArr()));
		segment.setDeparture(LocalityServiceController.getLocality(String.valueOf(travel.getTrip().getSrc().getIdx())));
		segment.setArrival(LocalityServiceController.getLocality(String.valueOf(travel.getTrip().getDst().getIdx())));
		Organisation carrier = new Organisation();
		carrier.setId(travel.getTrip().getProviderId());
		carrier.setName(Lang.UA, travel.getTrip().getTransporter());
		segment.setCarrier(carrier);
		Organisation insurance = new Organisation();
		insurance.setId(StringUtil.md5(counterparts.getInsurer().getName()));
		insurance.setName(Lang.UA, counterparts.getInsurer().getName());
		segment.setInsurance(insurance);
		
		return segment;
	}
	
	@JsonIgnore
	public Seat getSeat() {
		if (soldSeats != null && !soldSeats.isEmpty()) {
			Seat seat = new Seat();
			seat.setId(soldSeats.get(0).getId());
			seat.setNumber(soldSeats.get(0).getId());
			return seat;
		} else {
			return null;
		}
	}
	
	@JsonIgnore
	public Price getPrice() {
		if (soldSeats != null && !soldSeats.isEmpty()) {
			Price price = new Price();
			price.setCurrency(Currency.UAH);
			price.setAmount(priceToBigDecimal(soldSeats.get(0).getPrice().getTotal()));
			price.setVat(priceToBigDecimal(soldSeats.get(0).getPrice().getTax()));
			// тариф и комиссии
			createTariffAndCommissions(price);
			return price;
		} else {
			return null;
		}
	}
	
	@JsonIgnore
	private void createTariffAndCommissions(Price price) {
		if (soldSeats != null && !soldSeats.isEmpty() && soldSeats.get(0).getPrice() != null
				&& soldSeats.get(0).getPrice().getArticles() != null) {
			Tariff priceTariff = null;
			for (InvoiceArticle tariff : soldSeats.get(0).getPrice().getArticles()) {
				if ("1001".equals(tariff.getCode()) || "1002".equals(tariff.getCode())) {
					priceTariff = new Tariff();
					priceTariff.setValue(priceToBigDecimal(tariff.getPrice()));
					priceTariff.setCode(tariff.getCode());
					priceTariff.setName(tariff.getName());
					price.setTariff(priceTariff);
				} else {
					if (price.getCommissions() == null) {
						price.setCommissions(new ArrayList<>());
					}
					Commission commission = new Commission();
					commission.setCode(tariff.getCode());
					commission.setName(tariff.getName());
					commission.setValue(priceToBigDecimal(tariff.getPrice()));
					commission.setType(ValueType.FIXED);
					commission.setValueCalcType(CalcType.OUT);
					price.getCommissions().add(commission);
				}
			}
			// если не смогли определить тариф по коду - берем с максимальной ценой
			if (priceTariff == null && price.getCommissions() != null) {
				Commission maxCommission = null;
				for (Commission commission : price.getCommissions()) {
					if (maxCommission == null || commission.getValue().compareTo(maxCommission.getValue()) > 0) {
						maxCommission = commission;
					}
				}
				if (maxCommission != null) {
					price.getCommissions().remove(maxCommission);
					priceTariff = new Tariff();
					priceTariff.setValue(maxCommission.getValue());
					priceTariff.setCode(maxCommission.getCode());
					priceTariff.setName(maxCommission.getName());
					price.setTariff(priceTariff);
				}
			}
		}
	}
	
	@JsonIgnore
	private BigDecimal priceToBigDecimal(long price) {
		return new BigDecimal(price).divide(new BigDecimal(100));
	}
}

