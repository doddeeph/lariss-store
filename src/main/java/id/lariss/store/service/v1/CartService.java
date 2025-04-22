package id.lariss.store.service.v1;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;
import java.util.Map;

public interface CartService {
    Map<String, Object> addToCart(Long productId, Long variantId, Integer quantity, Long customerId);

    CartDTO addToCart(CartItemDTO cartItemDTO);

    Map<String, String> getCart(Long customerId);
}
