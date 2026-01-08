package dev.vundirov.domain.repositories;

import dev.vundirov.domain.entity.WarehouseIdempotentMessagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseIdempotentMessagesEntityRepository extends JpaRepository<WarehouseIdempotentMessagesEntity, Long> {
}