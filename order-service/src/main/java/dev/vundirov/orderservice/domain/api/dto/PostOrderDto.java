package dev.vundirov.orderservice.domain.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for {@link dev.vundirov.orderservice.domain.entities.Order}
 */
public record PostOrderDto(@NotNull Integer userId,
                           @NotNull BigDecimal totalCost,
                           Set<PostOrderItemDto> orderItems) {
}