package com.gillsoft;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.gillsoft.abstract_rest_service.SimpleAbstractTripSearchService;
import com.gillsoft.cache.CacheHandler;
import com.gillsoft.client.ResponseError;
import com.gillsoft.client.RestClient;
import com.gillsoft.client.TripPackage;
import com.gillsoft.client.model.Var;
import com.gillsoft.model.Currency;
import com.gillsoft.model.Document;
import com.gillsoft.model.Locality;
import com.gillsoft.model.Organisation;
import com.gillsoft.model.Price;
import com.gillsoft.model.RequiredField;
import com.gillsoft.model.RestError;
import com.gillsoft.model.ReturnCondition;
import com.gillsoft.model.Route;
import com.gillsoft.model.Seat;
import com.gillsoft.model.SeatsScheme;
import com.gillsoft.model.Segment;
import com.gillsoft.model.Tariff;
import com.gillsoft.model.Trip;
import com.gillsoft.model.TripContainer;
import com.gillsoft.model.Vehicle;
import com.gillsoft.model.request.TripSearchRequest;
import com.gillsoft.model.response.TripSearchResponse;
import com.gillsoft.util.StringUtil;

@RestController
public class SearchServiceController extends SimpleAbstractTripSearchService<TripPackage> {
	
	@Autowired
	private RestClient client;
	
	@Autowired
	@Qualifier("MemoryCacheHandler")
	private CacheHandler cache;

	@Override
	public List<ReturnCondition> getConditionsResponse(String arg0, String arg1) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public List<Document> getDocumentsResponse(String arg0) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public List<Tariff> getTariffsResponse(String arg0) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public List<RequiredField> getRequiredFieldsResponse(String arg0) {
		return Arrays.asList(RequiredField.NAME, RequiredField.SURNAME, RequiredField.PHONE, RequiredField.EMAIL);
	}

	@Override
	public Route getRouteResponse(String arg0) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public SeatsScheme getSeatsSchemeResponse(String arg0) {
		throw RestClient.createUnavailableMethod();
	}
	
	@Override
	public List<Seat> updateSeatsResponse(String arg0, List<Seat> arg1) {
		throw RestClient.createUnavailableMethod();
	}
	
	@Override
	public List<Seat> getSeatsResponse(String tripId) {
		try {
			return client.getSeats(tripId);
		} catch (Exception e) {
			throw new RestClientException(e.getMessage());
		}
	}

	@Override
	public TripSearchResponse initSearchResponse(TripSearchRequest request) {
		return simpleInitSearchResponse(cache, request);
	}
	
	@Override
	public void addInitSearchCallables(List<Callable<TripPackage>> callables, String[] pair, Date date) {
		callables.add(() -> {
			try {
				validateSearchParams(pair, date);
				TripPackage tripPackage = client.getCachedTrips(pair[0], pair[1], date);
				if (tripPackage == null) {
					throw new ResponseError("Empty result");
				}
				tripPackage.setRequest(TripSearchRequest.createRequest(pair, date));
				return tripPackage;
			} catch (ResponseError e) {
				TripPackage tripPackage = new TripPackage();
				tripPackage.setError(e);
				tripPackage.setRequest(TripSearchRequest.createRequest(pair, date));
				return tripPackage;
			} catch (Exception e) {
				return null;
			}
		});
	}
	
	private static void validateSearchParams(String[] pair, Date date) throws ResponseError {
		if (date == null
				|| date.getTime() < DateUtils.truncate(new Date(), Calendar.DATE).getTime()) {
			throw new ResponseError("Invalid parameter \"date\"");
		}
		if (pair == null || pair.length < 2) {
			throw new ResponseError("Invalid parameter \"pair\"");
		}
	}
	
	@Override
	public TripSearchResponse getSearchResultResponse(String searchId) {
		return simpleGetSearchResponse(cache, searchId);
	}
	
	@Override
	public void addNextGetSearchCallablesAndResult(List<Callable<TripPackage>> callables, Map<String, Vehicle> vehicles,
			Map<String, Locality> localities, Map<String, Organisation> organisations, Map<String, Segment> segments,
			List<TripContainer> containers, TripPackage tripPackage) {
		if (!tripPackage.isContinueSearch()) {
			addResult(vehicles, localities, segments, containers, tripPackage);
		} else if (tripPackage.getRequest() != null) {
			addInitSearchCallables(callables, tripPackage.getRequest().getLocalityPairs().get(0),
					tripPackage.getRequest().getDates().get(0));
		}
	}
	
	private void addResult(Map<String, Vehicle> vehicles, Map<String, Locality> localities,
			Map<String, Segment> segments, List<TripContainer> containers, TripPackage tripPackage) {
		TripContainer container = new TripContainer();
		container.setRequest(tripPackage.getRequest());
		if (tripPackage != null
				&& tripPackage.getVars() != null) {
			
			List<Trip> trips = new ArrayList<>();
			
			for (Var trip : tripPackage.getVars()) {
				// если рейс заблокирован/уехал/продан - не добавляем
				if (trip.getTrip().isNotForSale()
						|| trip.getSeats().getBusSoft().getFree().compareTo(0) == 0) {
					continue;
				}
				Trip tmpTrip = new Trip();
				tmpTrip.setId(trip.getGuididx());
				trips.add(tmpTrip);
				
				String vehicleId = StringUtil.md5(trip.getTrip().getHardware());
				Vehicle vehicle = vehicles.get(vehicleId);
				if (vehicle == null) {
					vehicle = new Vehicle(vehicleId);
					vehicle.setModel(trip.getTrip().getHardware());
					vehicles.put(vehicleId, vehicle);
				}
				
				String segmentId = tmpTrip.getId();
				Segment segment = segments.get(segmentId);
				if (segment == null) {
					segment = new Segment();
					segment.setId(trip.getGuididx());
					segment.setNumber(trip.getTrip().getId());
					try {
						segment.setDepartureDate(com.gillsoft.util.Date.getFullDateString(trip.getArrDate(), trip.getDstArr()));
						segment.setArrivalDate(com.gillsoft.util.Date.getFullDateString(trip.getArrDate(), trip.getDstArr()));
					} catch (Exception e) {}
					if (vehicles.containsKey(vehicleId)) {
						segment.setVehicle(new Vehicle(vehicleId));
					}
					segment.setFreeSeatsCount(trip.getSeats().getBusSoft().getFree());
					segments.put(segmentId, segment);
				}
				
				segment.setDeparture(addStation(localities, String.valueOf(trip.getTrip().getSrc().getIdx())));
				segment.setArrival(addStation(localities, String.valueOf(trip.getTrip().getDst().getIdx())));
				
				addPrice(segment, new BigDecimal(trip.getSeats().getBusSoft().getPrice()).divide(new BigDecimal(100)));
				
			}
			container.setTrips(trips);
		}
		if (tripPackage.getError() != null) {
			container.setError(new RestError(tripPackage.getError().getMessage()));
		}
		containers.add(container);
	}
	
	private void addPrice(Segment segment, BigDecimal price) {
		Price tripPrice = new Price();
		Tariff tariff = new Tariff();
		tariff.setValue(price);
		tripPrice.setCurrency(Currency.UAH);
		tripPrice.setAmount(price);
		tripPrice.setTariff(tariff);
		segment.setPrice(tripPrice);
	}
	
	public static void addVehicle(Map<String, Vehicle> vehicles, Segment segment, String model) {
		String vehicleKey = StringUtil.md5(model);
		Vehicle vehicle = vehicles.get(vehicleKey);
		if (vehicle == null) {
			vehicle = new Vehicle();
			vehicle.setModel(model);
			vehicles.put(vehicleKey, vehicle);
		}
		segment.setVehicle(new Vehicle(vehicleKey));
	}
	
	public static Locality addStation(Map<String, Locality> localities, String id) {
		Locality locality = LocalityServiceController.getLocality(id);
		if (locality == null) {
			return null;
		}
		String localityId = locality.getId();
		try {
			locality = locality.clone();
			locality.setId(null);
		} catch (CloneNotSupportedException e) {
		}
		if (!localities.containsKey(localityId)) {
			localities.put(localityId, locality);
		}
		return new Locality(localityId);
	}

}
