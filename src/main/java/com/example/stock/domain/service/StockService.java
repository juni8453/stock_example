package com.example.stock.domain.service;

import com.example.stock.domain.Stock;
import com.example.stock.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockService {

  private final StockRepository stockRepository;

  // 재고 감소
  @Transactional
  public void decrease(Long id, Long quantity) {
    Stock findStock = stockRepository.findById(id).orElseThrow();

    findStock.decrease(quantity);

    stockRepository.saveAndFlush(findStock);
  }
}
