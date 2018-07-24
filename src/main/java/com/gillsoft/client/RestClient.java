package com.gillsoft.client;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gillsoft.cache.CacheHandler;
import com.gillsoft.cache.IOCacheException;
import com.gillsoft.cache.RedisMemoryCache;
import com.gillsoft.logging.SimpleRequestResponseLoggingInterceptor;
import com.gillsoft.model.BillOptions;
import com.gillsoft.model.BillRequest;
import com.gillsoft.model.BillSeat;
import com.gillsoft.model.CarMap;
import com.gillsoft.model.Customer;
import com.gillsoft.model.Invoice;
import com.gillsoft.model.Lang;
import com.gillsoft.model.Locality;
import com.gillsoft.model.Point;
import com.gillsoft.model.Seat;
import com.gillsoft.model.SeatStatus;
import com.gillsoft.model.SeatType;
import com.gillsoft.model.ServiceItem;
import com.gillsoft.model.Stations;
import com.gillsoft.model.request.Request;
import com.gillsoft.util.RestTemplateUtil;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RestClient {
	
	public enum InvoiceStatus {
		CREATED (0, "Создан"),
		RESERVATION (1, "Выполняется резервирование мест"),
		RESERVATION_ERROR (2, "Ошибка при резервировании мест"),
		RESERVED (3, "Места зарезервированы, инвойс ожидает оплаты"),
		PAID (4, "Инвойс оплачен)"),
		PAY_TIME_EXPIRED (5, "Время ожидания оплаты истекло"),
		PRINTED (6, "Распечатан проездной документ в кассе"),
		CANCEL_CARRIER_ERROR (7, "Ошибка при отмене у поставщика"),
		CANCELED (8, "Аннулирован"),
		CANCEL_ERROR (9, "Ошибка при отмене"),
		DELETED (10, "Удален"),
		CANCELLATION (11, "Отменяется"),
		OTHER (12, "");

		private int status;
		private String description;
		
		private InvoiceStatus(int status, String description) {
			this.status = status;
			this.description = description;
		}
		
		public int getStatus() {
			return this.status;
		}
		
		public String getDescription() {
			return this.description;
		}
	}

	public static final String STATIONS_CACHE_KEY = "codrive.stations.";
	public static final String TRIPS_CACHE_KEY = "codrive.trips.";

	public static final String CODE_DEPARTURE = "D";
	public static final String CODE_ARRIVE = "A";

	private static final String NAME_REGEX = "[^a-zA-Z0-9\u0430-\u044F\u0410-\u042F\u0456\u0406\u0457\u0407\u0454\u0404-]";

	private static final String STOPPOINTS = "api/rd/stoppoint/ua/bus";
	private static final String DEPARTURE_STATIONS = "api/departure_stations/ext/%s/bus";
	private static final String ARRIVE_STATIONS = "api/arrive_stations/ext/%s/bus";
	private static final String VARIANTS = "api/variants/ua/bus/%s/%s/%s";
	private static final String CAR_MAP = "api/car_map/ua/%s/0";
	private static final String BILL = "api/bill/ua/%s/%s";
	private static final String INVOICE = "api/invoice/ua/%s";
	private static final String PAY = "/api/pay/%s/%s";
	private static final String CANCEL = "/api/cancel/%s";

	private static HttpHeaders headers = new HttpHeaders();
	private static HttpHeaders postHeaders = new HttpHeaders();

	private static final List<String> LANGS = Arrays.asList(new String[] { "ua", "ru", "en" });

    @Autowired
    @Qualifier("RedisMemoryCache")
	private CacheHandler cache;
   
    static {
    	headers.add("Authorization", "Basic "
	            + new String(Base64.encodeBase64String((Config.getLogin() + ":" + Config.getPassword()).getBytes())));
    	postHeaders.addAll(headers);
    	postHeaders.add("Content-Type", "application/json; charset=utf-8");
    }

	private RestTemplate template;

	// для запросов поиска с меньшим таймаутом
	private RestTemplate searchTemplate;

	public RestClient() {
		template = createNewPoolingTemplate(Config.getRequestTimeout());
		searchTemplate = createNewPoolingTemplate(Config.getSearchRequestTimeout());
	}

	public RestTemplate createNewPoolingTemplate(int requestTimeout) {
		HttpComponentsClientHttpRequestFactory factory = (HttpComponentsClientHttpRequestFactory) RestTemplateUtil
				.createPoolingFactory(Config.getUrl(), 300, requestTimeout);
		factory.setReadTimeout(Config.getReadTimeout());
		RestTemplate template = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
		template.setInterceptors(Collections.singletonList(new SimpleRequestResponseLoggingInterceptor()));
		return template;
	}

	/****************** STATIONS ********************/
	@SuppressWarnings("unchecked")
	public List<Locality> getCachedStations() throws IOCacheException {
		Map<String, Object> params = new HashMap<>();
		params.put(RedisMemoryCache.OBJECT_NAME, RestClient.STATIONS_CACHE_KEY);
		params.put(RedisMemoryCache.IGNORE_AGE, true);
		params.put(RedisMemoryCache.UPDATE_DELAY, Config.getCacheStationsUpdateDelay());
		params.put(RedisMemoryCache.UPDATE_TASK, new StationsUpdateTask());
		return (List<Locality>) cache.read(params);
	}

	public List<Locality> getAllStations() throws ResponseError {
		Map<BigDecimal, Locality> localities = new HashMap<>();
		try {
			for (String lang : LANGS) {
				Stations stations = getResult(template, null, String.format(DEPARTURE_STATIONS, lang), HttpMethod.GET,
						new ParameterizedTypeReference<Stations>() { });
				if (stations != null && stations.getStations() != null && !stations.getStations().isEmpty()) {
					Stations arrStations = getResult(template, null, String.format(ARRIVE_STATIONS, lang), HttpMethod.GET,
							new ParameterizedTypeReference<Stations>() { });
					boolean isArrivalEmpty = true;
					if (arrStations != null && arrStations.getStations() != null && !arrStations.getStations().isEmpty()) {
						stations.getStations().addAll(arrStations.getStations());
						isArrivalEmpty = false;
					}
					Lang langTmp = Lang.valueOf(lang.toUpperCase());
					for (Point point : stations.getStations()) {
						Locality locality = localities.get(point.getIdx());
						if (locality != null) {
							locality.setName(langTmp, point.getName());
						} else {
							locality = new Locality();
							locality.setId(String.valueOf(point.getIdx()));
							locality.setName(langTmp, point.getName());
							locality.setLatitude(point.getLatitude());
							locality.setLongitude(point.getLongitude());
							locality.setDetails(isArrivalEmpty ? CODE_DEPARTURE : CODE_DEPARTURE + (stations.getStations().contains(point) ? CODE_ARRIVE : ""));
							localities.put(point.getIdx(), locality);
						}
					}
				}
			}
			return new ArrayList<>(localities.values());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/****************** SEATS ********************/
	public List<Seat> getSeats(String tripId) throws ResponseError {
		List<Seat> seats = new ArrayList<>();
		CarMap carMap = getResult(searchTemplate, null, String.format(CAR_MAP, tripId), HttpMethod.GET, new ParameterizedTypeReference<CarMap>() { });
		if (carMap.getFreePlaces() != null && !carMap.getFreePlaces().isEmpty()) {
			carMap.getFreePlaces().stream().forEach(c -> {
				seats.add(createSeat(c, SeatStatus.FREE));
			});
		}
		if (carMap.getBusyPlaces() != null && !carMap.getBusyPlaces().isEmpty()) {
			carMap.getBusyPlaces().stream().forEach(c -> {
				seats.add(createSeat(c, SeatStatus.SALED));
			});
		}
		return seats;
	}
	
	private Seat createSeat(String seatId, SeatStatus seatStatus) {
		Seat newSeat = new Seat();
		newSeat.setId(seatId);
		newSeat.setNumber(seatId);
		newSeat.setStatus(seatStatus);
		newSeat.setType(SeatType.SEAT);
		return newSeat;
	}

	/****************** TRIPS ********************/
	public TripPackage getCachedTrips(String from, String to, Date dispatch) throws ResponseError {
		String key = String.format(VARIANTS, from, to, com.gillsoft.util.Date.dateFormat.format(dispatch));
		Map<String, Object> params = new HashMap<>();
		params.put(RedisMemoryCache.OBJECT_NAME, key);
		params.put(RedisMemoryCache.UPDATE_TASK, new GetTripsTask(key));
		try {
			return (TripPackage) cache.read(params);
		} catch (IOCacheException e) {
			e.printStackTrace();
			// ставим пометку, что кэш еще формируется
			TripPackage tripPackage = new TripPackage();
			tripPackage.setContinueSearch(true);
			return tripPackage;
		} catch (Exception e) {
			throw new ResponseError(e.getMessage());
		}
	}

	public TripPackage getTrips(String key) throws ResponseError {
		return getResult(searchTemplate, null, key, HttpMethod.GET, new ParameterizedTypeReference<TripPackage>() { });
	}
	
	/****************** BILL/PAY/CANCEL ********************/
	private Invoice getInvoice(RequestEntity<?> requestEntity) throws ResponseError {
		return template.exchange(requestEntity, Invoice.class).getBody();
	}
	
	private void checkInvoice(Invoice invoice) throws ResponseError {
		if (invoice == null) {
            throw new ResponseError("Не получен заказ от ресурса.");
        }
        if (invoice.getError() != null) {
        	throw new ResponseError(String.join("|", "Произошла ошибка при создании заказа.", invoice.getError().getCode(), invoice.getError().getMessage()));
        }
	}
	
	public Invoice bill(ServiceItem service, Customer customer) throws ResponseError {
		try {
			Invoice invoice = getInvoice(getRequestEntity(
					new BillRequest(
							new BillSeat(customer.getName().replaceAll(NAME_REGEX, ""), customer.getSurname().replaceAll(NAME_REGEX, "")),
							new BillOptions(customer.getEmail(), customer.getPhone())),
					HttpMethod.POST, String.format(BILL, service.getSegment().getId(), service.getSeat().getId())));
			checkInvoice(invoice);
            service.setId(invoice.getId());
            service.setNumber(invoice.getAspsCode2());
            return invoice;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void pay(String invoiceId) throws ResponseError {
		Invoice invoice = null;
		try {
			invoice = getInvoice(getRequestEntity(null, HttpMethod.PUT,
					String.format(PAY, invoiceId, UUID.randomUUID().toString().replaceAll("-", ""))));
			checkInvoice(invoice);
			if (!invoice.getState().equals(InvoiceStatus.PAID.getStatus())) {
				throw new ResponseError("Ошибка выкупа заказа. Статус заказа - " + invoice.getState());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void cancel(String invoiceId) throws ResponseError {
		Invoice invoice = null;
		try {
			invoice = getInvoice(getRequestEntity(
					null,
					HttpMethod.DELETE, String.format(CANCEL, invoiceId)));
			checkInvoice(invoice);
			if (!invoice.getState().equals(InvoiceStatus.CANCELED.getStatus())) {
				throw new ResponseError("Ошибка отмены заказа. Статус заказа - " + invoice.getState());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*************************************************/
	private <T> T getResult(RestTemplate template, Request request, String method, HttpMethod httpMethod,
			ParameterizedTypeReference<T> type) throws ResponseError {
		URI uri = UriComponentsBuilder.fromUriString(Config.getUrl() + method).build().toUri();
		RequestEntity<Request> requestEntity = new RequestEntity<Request>(request,
				(httpMethod.equals(HttpMethod.POST) ? postHeaders : headers), httpMethod, uri);
		ResponseEntity<T> response = template.exchange(requestEntity, type);
		return response.getBody();
	}
	
	private <T> RequestEntity<T> getRequestEntity(T request, HttpMethod httpMethod, String method) {
		return new RequestEntity<T>(request, (httpMethod.equals(HttpMethod.POST) ? postHeaders : headers), httpMethod,
				UriComponentsBuilder.fromUriString(Config.getUrl() + method).build().toUri());
	}

	public CacheHandler getCache() {
		return cache;
	}

	public static RestClientException createUnavailableMethod() {
		return new RestClientException("Method is unavailable");
	}

}
