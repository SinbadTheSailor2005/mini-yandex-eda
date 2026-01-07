package dev.vundirov.orderservice.domain.repo;

import dev.vundirov.orderservice.domain.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}