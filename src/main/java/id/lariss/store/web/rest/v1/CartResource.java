package id.lariss.store.web.rest.v1;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;
import id.lariss.store.service.v1.CartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("CartResourceV1")
@RequestMapping("/api/v1/carts")
public class CartResource {

    private static final Logger LOG = LoggerFactory.getLogger(CartResource.class);

    private final CartService cartService;

    public CartResource(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartDTO> addToCart(@Valid @RequestBody CartItemDTO cartItemDTO) {
        CartDTO cartDTO = cartService.addToCart(cartItemDTO);
        return ResponseEntity.ok().body(cartDTO);
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestParam Long customerId) {
        CartDTO cartDTO = cartService.getCart(customerId);
        return ResponseEntity.ok().body(cartDTO);
    }
}
