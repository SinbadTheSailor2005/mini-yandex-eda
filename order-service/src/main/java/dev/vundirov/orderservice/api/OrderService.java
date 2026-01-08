package dev.vundirov.orderservice.api;

import dev.vundirov.common.config.KafkaConfiguration;
import dev.vundirov.common.dto.OrderDto;
import dev.vundirov.orderservice.api.dto.OrderMapper;
import dev.vundirov.orderservice.api.dto.get.RequestGetOrderDto;
import dev.vundirov.orderservice.api.dto.post.RequestPostOrderDto;
import dev.vundirov.orderservice.domain.entity.OrderEntity;
import dev.vundirov.orderservice.domain.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {
  private static final Logger logger =
          LoggerFactory.getLogger(OrderService.class);

  private final OrderMapper orderMapper;

  private final OrderRepository orderRepository;

  private final KafkaTemplate<String, OrderDto> kafkaTemplate;

  public RequestGetOrderDto getOne(Long id) {
    Optional<OrderEntity> orderEntityOptional = orderRepository.findById(id);
    return orderMapper.toRequestGetOrderDto(
            orderEntityOptional.orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Entity with id `%s` not found".formatted(id)
                    )));
  }

  // чтобы save и отправку в кафку были атомарны
  @Transactional
  public RequestPostOrderDto create(RequestPostOrderDto dto) {
    logger.info("Creating order: {}", dto);
    OrderEntity orderEntity = orderMapper.toEntity(dto);

    OrderEntity savedOrder = orderRepository.save(orderEntity);
    OrderDto orderDto = orderMapper.toSharedOrderDto(savedOrder);
    logger.info("Sending new order {} to kafka..", orderDto.id());
    kafkaTemplate.send(KafkaConfiguration.ORDER_CREATED_TOPIC, orderDto.id().toString(),
            orderDto);
    RequestPostOrderDto response = orderMapper.toOrderDto(savedOrder);
    logger.info("Created: {}", response);
    return response;
  }

  @KafkaListener(
          topics = KafkaConfiguration.PAYMENT_FAILED_TOPIC,
          groupId = "payment-failed-group",
          containerFactory = "orderDtoListenerFactory"
  )
  public void handleFailedPayment(OrderDto dto) {
    logger.info("Payment was failed. Handling error..");
    orderRepository.findById(dto.id())
            .ifPresent(orderEntity ->
                    {
                      orderRepository.save(orderEntity);
                      logger.info(
                              "Order {} status updated to {} ", dto.id(),
                              orderEntity.getPaymentStatus()
                      );
                    }
            );
  }
  @KafkaListener(
          topics = KafkaConfiguration.NO_ITEMS_WAREHOUSE_CANCELLED_TOPIC,
          groupId = "warehouse-cancelled-group",
          containerFactory ="orderDtoListenerFactory"
  )
  public void handleFailedWarehouseCheck(OrderDto order) {
    logger.info("No items is warehouse. Handling failed order...");
  }
}
