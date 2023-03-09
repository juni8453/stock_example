package com.example.stock.repository;

import com.example.stock.domain.Stock;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT s FROM Stock s WHERE s.id = :id")
  Stock findByIdWithPessimisticLock(@Param(value = "id") Long id);

  @Lock(value = LockModeType.OPTIMISTIC)
  @Query("SELECT s FROM Stock s WHERE s.id = :id")
  Stock findByIdWithOptimisticLock(@Param(value = "id") Long id);
}
