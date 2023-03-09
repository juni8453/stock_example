package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// OptimisticLock Update 실패 시 재시도를 위한 클래스
@RequiredArgsConstructor
@Service
public class OptimisticLockStockFacade {

  private final OptimisticLockStockService optimisticLockStockService;

  public void decrease(Long id, Long quantity) throws InterruptedException {
    while (true) {
      try {
        optimisticLockStockService.decrease(id, quantity);
        break;

      } catch (Exception e) {
        Thread.sleep(50);
      }
    }
  }
}
