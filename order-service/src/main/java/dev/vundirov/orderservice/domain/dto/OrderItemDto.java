package dev.vundirov.orderservice.domain.dto;

import dev.vundirov.orderservice.domain.entities.OrderItem;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO for {@link OrderItem}
 */
public record OrderItemDto(Integer id, @NotNull BigDecimal priceAtPurchase,
                           @NotNull Integer quantity) {
}