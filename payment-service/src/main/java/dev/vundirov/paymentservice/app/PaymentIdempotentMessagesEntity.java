package dev.vundirov.paymentservice.app;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_idempotent_messages")
public class PaymentIdempotentMessagesEntity {
  @Id
  @Column(name = "message_key", nullable = false)
  private String messageKey;

  private LocalDateTime createdAt = LocalDateTime.now();


}