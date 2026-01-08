package dev.vundirov.domain.repositories;

import dev.vundirov.domain.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemsRepository extends JpaRepository<Items, Long> {

  @Modifying
  @Query("UPDATE Items i SET i.quantity = i.quantity - :amount " +
          "WHERE i.id = :id AND i.quantity >= :amount")
  int reserveItem(@Param("id") Long id, @Param("amount") Long amount);

  @Modifying
  @Query("UPDATE Items i SET i.quantity = i.quantity + :amount " +
          "WHERE i.id = :id")
  int returnItem(@Param("id") Long id, @Param("amount") Long amount);
}