package dev.vundirov.orderservice.warehouse;

public interface Warehouse {

  public boolean checkItem(long id, int quantity);
}
