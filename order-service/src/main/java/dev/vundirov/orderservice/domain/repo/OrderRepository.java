package dev.vundirov.orderservice.domain.repo;

import dev.vundirov.orderservice.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}