package id.lariss.store.web.rest.v1;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.OrderDTO;
import id.lariss.store.service.v1.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("OrderResourceV1")
@RequestMapping("/api/v1/orders")
public class OrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@Valid @RequestBody CartDTO cartDTO) {
        OrderDTO savedOrderDTO = orderService.placeOrder(cartDTO, "");
        return ResponseEntity.ok().body(savedOrderDTO);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getOrder(@RequestParam Long customerId) {
        List<Map<String, Object>> response = orderService.getOrders(customerId);
        return ResponseEntity.ok().body(response);
    }
}
