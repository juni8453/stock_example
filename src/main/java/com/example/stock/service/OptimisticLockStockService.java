package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OptimisticLockStockService {

  private final StockRepository stockRepository;

  @Transactional
  public void decrease(Long id, Long quantity) {
    Stock findStock = stockRepository.findByIdWithOptimisticLock(id);

    findStock.decrease(quantity);

    stockRepository.saveAndFlush(findStock);
  }
}
