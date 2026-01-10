package dev.vundirov.orderservice.domain.api;


import dev.vundirov.orderservice.domain.api.dto.PostOrderDto;
import dev.vundirov.orderservice.domain.dto.OrderDto;
import dev.vundirov.orderservice.domain.entities.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/{id}")
  public OrderDto getOne(@PathVariable Integer id) {
    return orderService.getOne(id);
  }

  @PostMapping
  public PostOrderDto create(@RequestBody @Valid PostOrderDto dto) {
    return orderService.create(dto);
  }
}
