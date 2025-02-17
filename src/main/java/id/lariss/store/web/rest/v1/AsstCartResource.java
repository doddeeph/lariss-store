package id.lariss.store.web.rest.v1;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;
import id.lariss.store.service.v1.AsstCartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
public class AsstCartResource {

    private static final Logger LOG = LoggerFactory.getLogger(AsstCartResource.class);

    private final AsstCartService cartService;

    public AsstCartResource(AsstCartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartItemDTO> addToCart(@Valid @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO savedCartItemDTO = cartService.addToCart(cartItemDTO);
        return ResponseEntity.ok().body(savedCartItemDTO);
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestParam Long customerId) {
        CartDTO cartDTO = cartService.getCart(customerId);
        return ResponseEntity.ok().body(cartDTO);
    }
}
