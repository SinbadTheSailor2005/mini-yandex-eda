package dev.vundirov.orderservice.payment;

import dev.vundirov.common.dto.OrderDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
@Service
public class PaymentService implements Payment{
  public static final String PAYMENT_PATH = "/api/v1/payment";
  private final RestClient restClient;

  public PaymentService(@Qualifier("warehouseRestClient") RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public OrderDto pay(OrderDto order) {
    if (order == null) return null;


    return restClient.post().uri(PAYMENT_PATH).body(order).retrieve().body(OrderDto.class); // retrieve = execute, body = serilization
  }
}
