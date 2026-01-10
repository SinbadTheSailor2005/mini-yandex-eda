package dev.vundirov.orderservice.domain.dto;

import dev.vundirov.orderservice.domain.PaymentStatus;
import dev.vundirov.orderservice.domain.entities.Order;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for {@link Order}
 */
public record OrderDto(Integer id, @NotNull PaymentStatus paymentStatus,
                       @NotNull Integer userId, @NotNull BigDecimal totalCost,
                       Set<OrderItemDto> orderItems) {
}