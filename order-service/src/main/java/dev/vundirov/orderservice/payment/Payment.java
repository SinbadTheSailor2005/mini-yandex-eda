package dev.vundirov.orderservice.payment;


import dev.vundirov.common.dto.OrderDto;

public interface Payment {
  OrderDto pay (OrderDto order);
}
