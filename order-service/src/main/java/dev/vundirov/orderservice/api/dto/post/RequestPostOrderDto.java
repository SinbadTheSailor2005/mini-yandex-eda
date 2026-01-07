package dev.vundirov.orderservice.api.dto.post;

import dev.vundirov.orderservice.domain.entity.OrderEntity;

import java.util.Set;

/**
 * DTO for {@link OrderEntity}
 */
public record RequestPostOrderDto(Set<RequestPostItemDto> items) {
}