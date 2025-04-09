package id.lariss.store.service.v1;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;

public interface CartService {
    CartDTO addToCart(CartItemDTO cartItemDTO);

    CartDTO getCart(Long customerId);
}
