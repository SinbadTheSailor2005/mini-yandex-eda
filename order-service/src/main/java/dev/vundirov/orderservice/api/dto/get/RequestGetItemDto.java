package dev.vundirov.orderservice.api.dto.get;

import dev.vundirov.orderservice.domain.entity.ItemEntity;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * DTO for {@link ItemEntity}
 */
public record RequestGetItemDto(Long id, String name,
                                @PositiveOrZero Long quantity,
                                BigDecimal cost) {
}