package dev.vundirov.paymentservice.app;

import dev.vundirov.common.config.KafkaConfiguration;
import dev.vundirov.common.dto.OrderDto;
import dev.vundirov.common.dto.PaymentStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
class PaymentListener {

  private static final Logger logger =
          LoggerFactory.getLogger(PaymentListener.class);

  private final KafkaTemplate<String, OrderDto> kafkaTemplate;
  private final IdempotentMessagesRepository idempotentMessagesRepository;

  @KafkaListener(
          topics = KafkaConfiguration.WAREHOUSE_READY_TOPIC,
          groupId = "payment-service-group",
          containerFactory = "orderDtoListenerFactory"
  )
  @Transactional
  public void handlePayment(OrderDto order) {
    String key = order.id()
            .toString();
    try {
      idempotentMessagesRepository.saveAndFlush(
              new PaymentIdempotentMessagesEntity(key, LocalDateTime.now())
      );
    } catch (DataIntegrityViolationException e) {
      logger.warn("Dublicate order {}. Skipping.", order.id());
      return;
    }
    boolean isPaid = pay(order);
    if (isPaid) {
      saveAndSendSuccessToKafka(order);
    } else {
      saveAndSendErrorToKafka(order);
    }
  }

  private void saveAndSendErrorToKafka(OrderDto order) {
    logger.error("Failed to pay order {}. Cancelling order", order.id());

    idempotentMessagesRepository.save(new PaymentIdempotentMessagesEntity(
            order.id()
                    .toString(),
            LocalDateTime.now()
    ));
    kafkaTemplate.send(
            KafkaConfiguration.PAYMENT_FAILED_TOPIC, order.id()
                    .toString(),
            new OrderDto(
                    order.id(), PaymentStatus.REJECTED,
                    order.total_cost(), order.items()
            )
    );
  }

  private void saveAndSendSuccessToKafka(OrderDto order) {
    logger.info("Successfully paid order {}", order.id());
    idempotentMessagesRepository.save(new PaymentIdempotentMessagesEntity(
            order.id()
                    .toString(),
            LocalDateTime.now()
    ));
    kafkaTemplate.send(
            KafkaConfiguration.PAYMENT_SUCCESS_TOPIC, order.id()
                    .toString(),
            new OrderDto(
                    order.id(), PaymentStatus.PAID, order.total_cost()
                    , order.items()
            )
    );
  }

  private boolean pay(OrderDto dto) {
    return true;
  }
}
