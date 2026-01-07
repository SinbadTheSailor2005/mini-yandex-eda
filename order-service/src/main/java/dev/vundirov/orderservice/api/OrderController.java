package dev.vundirov.orderservice.api;

import dev.vundirov.orderservice.api.dto.get.RequestGetOrderDto;
import dev.vundirov.orderservice.api.dto.post.RequestPostOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

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
