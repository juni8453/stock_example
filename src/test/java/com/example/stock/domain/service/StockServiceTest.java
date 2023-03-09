package com.example.stock.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.stock.domain.Stock;
import com.example.stock.domain.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

  @Autowired
  private StockService stockService;

  @Autowired
  private StockRepository stockRepository;

  @BeforeEach
  public void before() {
    Stock stock = new Stock(1L, 100L);
    stockRepository.saveAndFlush(stock);
  }

  @AfterEach
  public void after() {
    stockRepository.deleteAll();
  }

  @Test
  public void 재고감소() {
    // when
    stockService.decrease(1L, 1L); // 1번 상품 재고 -1

    // then
    Stock findStock = stockRepository.findById(1L).get();

    assertThat(findStock.getQuantity()).isEqualTo(99);
  }
}