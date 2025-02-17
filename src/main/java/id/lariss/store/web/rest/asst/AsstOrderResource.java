package id.lariss.store.web.rest.asst;

import id.lariss.store.service.asst.AsstOrderService;
import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.OrderDTO;
import jakarta.validation.Valid;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asst/orders")
public class AsstOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(AsstOrderResource.class);

    private final AsstOrderService orderService;

    public AsstOrderResource(AsstOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@Valid @RequestBody CartDTO cartDTO) {
        OrderDTO savedOrderDTO = orderService.placeOrder(cartDTO);
        return ResponseEntity.ok().body(savedOrderDTO);
    }

    @GetMapping
    public ResponseEntity<Set<OrderDTO>> getOrder(@RequestParam Long customerId) {
        Set<OrderDTO> orderDTOs = orderService.getOrders(customerId);
        return ResponseEntity.ok().body(orderDTOs);
    }
}
