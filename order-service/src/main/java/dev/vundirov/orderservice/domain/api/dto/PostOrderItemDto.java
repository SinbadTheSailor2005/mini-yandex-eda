package dev.vundirov.orderservice.domain.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO for {@link dev.vundirov.orderservice.domain.entities.OrderItem}
 */
public record PostOrderItemDto(Integer id, @NotNull BigDecimal priceAtPurchase,
                               @NotNull Integer quantity) {
}