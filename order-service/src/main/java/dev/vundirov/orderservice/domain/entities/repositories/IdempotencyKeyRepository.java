package dev.vundirov.orderservice.domain.entities.repositories;

import dev.vundirov.orderservice.domain.entities.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {
}