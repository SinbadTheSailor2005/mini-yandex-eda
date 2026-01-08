package dev.vundirov.paymentservice.app;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotentMessagesRepository extends JpaRepository<PaymentIdempotentMessagesEntity, String> {
}