package com.example.stock.facade;

import com.example.stock.service.StockService;
import com.example.stock.repository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LettuceLockStockFacade {

  private final RedisLockRepository redisLockRepository;
  private final StockService stockService;

  public void decrease(Long key, Long quantity) throws InterruptedException {
    while (!redisLockRepository.lock(key)) {
      Thread.sleep(100);
    }

    try {
      stockService.decrease(key, quantity);

    } finally {
      redisLockRepository.unlock(key);
    }

  }

}
