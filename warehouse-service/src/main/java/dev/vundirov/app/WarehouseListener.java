package dev.vundirov.app;

import dev.vundirov.common.config.KafkaConfiguration;
import dev.vundirov.common.dto.ItemDto;
import dev.vundirov.common.dto.OrderDto;
import dev.vundirov.domain.entity.WarehouseIdempotentMessagesEntity;
import dev.vundirov.domain.repositories.ItemsRepository;
import dev.vundirov.domain.repositories.WarehouseIdempotentMessagesEntityRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
class WarehouseListener {
  private final Random random = new Random();
  private static final Logger logger =
          LoggerFactory.getLogger(WarehouseListener.class);

  private final KafkaTemplate<String, OrderDto> kafkaTemplate;
  private final WarehouseIdempotentMessagesEntityRepository
          warehouseIdempotentMessagesEntityRepository;
  private final ItemsRepository itemsRepository;

  @KafkaListener(
          topics = KafkaConfiguration.ORDER_CREATED_TOPIC,
          groupId = "warehouse-service-group",
          containerFactory = "orderDtoListenerFactory"
  )
  @Transactional
  public void handleItemsAvailability(OrderDto order) {

    try {
      warehouseIdempotentMessagesEntityRepository.saveAndFlush(
              new WarehouseIdempotentMessagesEntity(
                      "order-create-"+order.id().toString(),
                      LocalDateTime.now()));
    } catch (DataIntegrityViolationException e) {
      logger.warn("Duplicate order {}. Skipping.", order.id());
      return;
    }
    try {

      reserveOrderItems(order);
      order = calculateCost(order);
      logger.info("All items are available in the warehouse. Reserved " +
              "required ones");
      kafkaTemplate.send(
              KafkaConfiguration.WAREHOUSE_READY_TOPIC, order.id()
                      .toString(), order
      );
    } catch (OutOfStockException e) {
      logger.error("Not enough items in the warehouse");
      kafkaTemplate.send(
              KafkaConfiguration.NO_ITEMS_WAREHOUSE_CANCELLED_TOPIC,
              order.id()
                      .toString(), order
      );
      throw new RuntimeException("Rollback transaction because OutOfStock", e);
    }

  }

  @Transactional // тут нужен?
  public void reserveOrderItems(OrderDto dto) {
    logger.info("Reserving items for order: {}", dto.id());

    for (var item : dto.items()) {
      int rowsUpdated = itemsRepository.reserveItem(item.id(), item.quantity());

      if (rowsUpdated == 0) {
        logger.error("Not enough quantity for item {}", item.id());
        throw new OutOfStockException("Item " + item.id() + " is out of stock");
      }
    }
  }

  @KafkaListener(
          topics = {KafkaConfiguration.PAYMENT_FAILED_TOPIC},
          groupId = "warehouse-cancel-items",
          containerFactory = "orderDtoListenerFactory"
  )
  @Transactional
  public void returnItems(OrderDto dto) {
    String rollbackKey = "payment-failed-" + dto.id();
    try {
      warehouseIdempotentMessagesEntityRepository.saveAndFlush(
              new WarehouseIdempotentMessagesEntity(
                      rollbackKey, LocalDateTime.now()));
    } catch (DataIntegrityViolationException e) {
      logger.warn("Rollback for order {} already processed. Skipping.", dto.id());
      return;
    }
    logger.info("Returning items from cancelled order {}", dto.id());
    for (var i : dto.items()) {
      itemsRepository.returnItem(i.id(), i.quantity());
    }
  }

  private OrderDto calculateCost(OrderDto order) {
    Set<ItemDto> itemsCost = new HashSet<>();
    long totalCost = getTotalCostAndCalculateItemPrice(order, itemsCost);
    return new OrderDto(
            order.id(), order.paymentStatus(),
            BigDecimal.valueOf(totalCost),
            itemsCost
    );
  }

  //TODO: вынести в отдельный сервис
  private long getTotalCostAndCalculateItemPrice(
          OrderDto order, Set<ItemDto> itemsCost) {
    long totalCost = 0;
    for (var i : order.items()) {
      int price = Math.abs(random.nextInt());
      long totalItemPrice = price * i.quantity();
      totalCost += totalItemPrice;
      itemsCost.add(new ItemDto(
              i.id(), i.name(), i.quantity(),
              BigDecimal.valueOf(totalItemPrice)
      ));
    }
    return totalCost;
  }


}
