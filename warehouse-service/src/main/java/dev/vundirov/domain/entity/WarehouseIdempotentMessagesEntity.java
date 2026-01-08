package dev.vundirov.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "warehouse_idempotent_messages_entity")
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseIdempotentMessagesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "time")
  private LocalDateTime time;

}