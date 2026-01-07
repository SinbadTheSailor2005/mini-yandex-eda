package dev.vundirov.orderservice.api;

import dev.vundirov.orderservice.api.dto.get.RequestGetOrderDto;
import dev.vundirov.orderservice.api.dto.post.RequestPostOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(OrderController.ORDER_PATH)
@RequiredArgsConstructor
public class OrderController {

  public static final String ORDER_PATH = "/api/v1/order";
  private final OrderService orderService;

  @PostMapping
  public RequestPostOrderDto create(@RequestBody RequestPostOrderDto dto) {
    return orderService.create(dto);
  }

  @GetMapping("/{id}")
  public RequestGetOrderDto getOne(@PathVariable Long id) {
    return orderService.getOne(id);
  }
}
