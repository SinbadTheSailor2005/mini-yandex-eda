package dev.vundirov.orderservice.warehouse;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WarehouseService implements Warehouse {
  Random random = new Random();
  @Override
  public boolean checkItem(long id, int quantity) {
    return random.nextBoolean();
  }
}
