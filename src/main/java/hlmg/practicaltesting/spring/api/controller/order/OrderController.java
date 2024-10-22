package hlmg.practicaltesting.spring.api.controller.order;

import hlmg.practicaltesting.spring.api.controller.order.request.OrderCreateRequest;
import hlmg.practicaltesting.spring.api.service.order.OrderService;
import hlmg.practicaltesting.spring.api.service.order.reponse.OrderResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime registeredDateTime = LocalDateTime.now();
        return orderService.createOrder(request, registeredDateTime);
    }
}