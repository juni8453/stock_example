package com.example.stock.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.stock.domain.Stock;
import com.example.stock.domain.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
  void 재고감소() {
    // when
    stockService.decrease(1L, 1L); // 1번 상품 재고 -1

    // then
    Stock findStock = stockRepository.findById(1L).orElseThrow();
    assertThat(findStock.getQuantity()).isEqualTo(99);
  }

  // Race Condition 발생
  // 둘 이상의 쓰레드가 공유 데이터에 엑세스 할 수 있고, 동시에 변경하려고 할 때 발생하는 상태
  // 1번 쓰레드가 데이터를 조회 후 갱신하기 전에 2번 쓰레드가 데이터를 조회하고 1번 쓰레드가 값을 갱신, 2번 쓰레드가 값을 갱신하기 떄문에
  // 재고가 5인 상태라고 한다면 둘 다 1을 줄인 값 즉, 4를 갱신하기 때문에 갱신이 누락되게 된다.
  @Test
  void 동시에_100개의_재고감소() throws Exception {
    int threadCount = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32); // 32 개의 쓰레드풀 생성
    CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 100 개의 쓰레드가 끝나면 다음 쓰레드를 실행

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          stockService.decrease(1L, 1L);
        } finally {
          countDownLatch.countDown(); // 쓰레드가 끝날 때 마다 카운트를 감소
        }
      });
    }

    countDownLatch.await(); // 카운트가 0이되면 대기가 풀리고 이후 쓰레드가 실행

    Stock findStock = stockRepository.findById(1L).orElseThrow();
    assertThat(findStock.getQuantity()).isEqualTo(0);
  }
}