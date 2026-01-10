package dev.vundirov.orderservice.domain.entities.repositories;

import dev.vundirov.orderservice.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}