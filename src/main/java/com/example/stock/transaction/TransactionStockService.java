package com.example.stock.transaction;

import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionStockService {

  private final StockService stockService;

  public void decrease(Long id, Long quantity) {
    startTransaction();

    stockService.decrease(id, quantity);

    // Transaction 종료 시점에 commit 이 발생하며 flush 가 호출 -> DB 에 값이 반영되는데 여기서 문제가 발생한다.
    // decrease() 호출 후 실행 완료 후 실제 DB 에 값을 갱신하기 전에 다른 쓰레드에서 decrease() 를 호출할 수 있기 때문.
    // 즉, 다른 쓰레드에서는 갱신 전 값을 가져가서 이전과 같은 Race Condition 이 발생하는 것.
    endTransaction();
  }


  private void startTransaction() {

  }

  private void endTransaction() {

  }
}
