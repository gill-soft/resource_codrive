package com.gillsoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.gillsoft.abstract_rest_service.AbstractOrderService;
import com.gillsoft.client.OrderIdModel;
import com.gillsoft.client.RestClient;
import com.gillsoft.client.RestClient.InvoiceStatus;
import com.gillsoft.client.ServiceIdModel;
import com.gillsoft.client.model.Invoice;
import com.gillsoft.model.Customer;
import com.gillsoft.model.Document;
import com.gillsoft.model.DocumentType;
import com.gillsoft.model.RestError;
import com.gillsoft.model.ServiceItem;
import com.gillsoft.model.request.OrderRequest;
import com.gillsoft.model.response.OrderResponse;
import com.gillsoft.util.StringUtil;

@RestController
public class OrderServiceController extends AbstractOrderService {
	
	@Autowired
	private RestClient client;

	@Override
	public OrderResponse addServicesResponse(OrderRequest request) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public OrderResponse bookingResponse(String orderId) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public OrderResponse cancelResponse(String orderId) {
		List<ServiceItem> resultItems = new ArrayList<>();
		OrderResponse response = new OrderResponse();
		// преобразовываем ид заказа в объект
		OrderIdModel orderIdModel = new OrderIdModel().create(orderId);
		response.setServices(new ArrayList<>(orderIdModel.getServices().size()));
		for (ServiceIdModel service : orderIdModel.getServices()) {
			try {
				client.cancel(service.getInvoiceId());
				addServiceItem(resultItems, service.getInvoiceId(), true, null);
			} catch (Exception e) {
				addServiceItem(resultItems, service.getInvoiceId(), false, new RestError(e.getMessage()));
			}
		}
		response.setOrderId(orderId);
		response.setServices(resultItems);
		return response;
	}

	@Override
	public OrderResponse createResponse(OrderRequest request) {
		// формируем ответ
		OrderResponse response = new OrderResponse();
		response.setCustomers(request.getCustomers());
		
		// копия для определения пассажиров
		List<ServiceItem> resultItems = new ArrayList<>();
		
		// список билетов
		OrderIdModel orderId = new OrderIdModel();
		
		for (ServiceItem service : request.getServices()) {
			try {
				Customer customer = request.getCustomers().get(service.getCustomer().getId());
				if (customer != null) {
					Invoice invoice = client.bill(service, customer);
					orderId.getServices().add(new ServiceIdModel(service.getId(), service.getNumber()));
					service.setCustomer(customer);
					service.setSegment(invoice.getSegment());
					service.setSeat(invoice.getSeat());
					service.setPrice(invoice.getPrice());
					resultItems.add(service);
				} else {
					throw new Exception("Customer not found - " + service.getCustomer().getId());
				}
			} catch (Exception e) {
				service.setError(new RestError(e.getMessage()));
				resultItems.add(service);
			}
		}
		response.setOrderId(orderId.asString());
		response.setServices(resultItems);
		return response;
	}

	@Override
	public OrderResponse getPdfDocumentsResponse(OrderRequest request) {
		OrderResponse response = new OrderResponse();
		OrderIdModel orderIdModel = new OrderIdModel().create(request.getOrderId());
		if (orderIdModel != null && orderIdModel.getServices() != null && !orderIdModel.getServices().isEmpty()) {
			Document document = new Document();
			document.setType(DocumentType.TICKET);
			document.setBase64(
					StringUtil.toBase64(client.getTickets(orderIdModel.getServices().get(0).getInvoiceId())));
			response.setDocuments(Arrays.asList(document));
		}
		return response;
	}

	@Override
	public OrderResponse getResponse(String orderId) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public OrderResponse getServiceResponse(String serviceId) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public OrderResponse confirmResponse(String orderId) {
		// формируем ответ
		OrderResponse response = new OrderResponse();
		List<ServiceItem> resultItems = new ArrayList<>();
		// преобразовываем ид заказа в объект
		OrderIdModel orderIdModel = new OrderIdModel().create(orderId);
		// выкупаем заказы и формируем ответ
		for (ServiceIdModel service : orderIdModel.getServices()) {
			try {
				client.pay(service.getInvoiceId());
				addServiceItem(resultItems, service.getInvoiceId(), true, null);
			} catch (Exception e) {
				addServiceItem(resultItems, service.getInvoiceId(), false, new RestError(e.getMessage()));
			}
		}
		response.setOrderId(orderId);
		response.setServices(resultItems);
		return response;
	}
	
	private void addServiceItem(List<ServiceItem> resultItems, String invoiceId, boolean confirmed, RestError error) {
		ServiceItem serviceItem = new ServiceItem();
		serviceItem.setId(invoiceId);
		serviceItem.setConfirmed(confirmed);
		serviceItem.setError(error);
		resultItems.add(serviceItem);
	}

	@Override
	public OrderResponse removeServicesResponse(OrderRequest request) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public OrderResponse returnServicesResponse(OrderRequest request) {
		OrderResponse response = new OrderResponse();
		response.setServices(new ArrayList<>(request.getServices().size()));
		for (ServiceItem serviceItem : request.getServices()) {
			
			// проверяем статус инвойса
			try {
				Invoice invoice = client.getInvoice(serviceItem.getId());
				int state = invoice.getState();
				if (state == InvoiceStatus.CANCELED.getStatus()
						|| state == InvoiceStatus.DELETED.getStatus()
						|| state == InvoiceStatus.PAY_TIME_EXPIRED.getStatus() 
						|| state == InvoiceStatus.OTHER.getStatus()) {
					addServiceItem(response.getServices(), serviceItem.getId(), true, null);
					continue;
				}
			} catch (Exception e) {
			}
			try {
				client.cancel(serviceItem.getId());
				addServiceItem(response.getServices(), serviceItem.getId(), true, null);
			} catch (Exception e) {
				addServiceItem(response.getServices(), serviceItem.getId(), false, new RestError(e.getMessage()));
			}
		}
		return response;
	}

	@Override
	public OrderResponse updateCustomersResponse(OrderRequest request) {
		throw RestClient.createUnavailableMethod();
	}

	@Override
	public OrderResponse prepareReturnServicesResponse(OrderRequest request) {
		OrderResponse response = new OrderResponse();
		response.setOrderId(request.getOrderId());
		response.setServices(request.getServices());
		return response;
	}

}
