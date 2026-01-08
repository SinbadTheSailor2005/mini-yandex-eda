package dev.vundirov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.vundirov")
class WarehouseServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(WarehouseServiceApplication.class, args);
  }

}
