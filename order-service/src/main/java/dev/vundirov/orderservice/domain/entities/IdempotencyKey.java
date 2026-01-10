package dev.vundirov.orderservice.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKey {
  @Id
  @Column(name = "message_id", nullable = false, length = Integer.MAX_VALUE)
  private String messageId;

}