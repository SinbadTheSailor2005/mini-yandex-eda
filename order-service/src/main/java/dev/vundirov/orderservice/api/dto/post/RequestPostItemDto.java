package dev.vundirov.orderservice.api.dto.post;

import dev.vundirov.orderservice.domain.entity.ItemEntity;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for {@link ItemEntity}
 */
public record RequestPostItemDto(String name, @PositiveOrZero Long quantity) {
}