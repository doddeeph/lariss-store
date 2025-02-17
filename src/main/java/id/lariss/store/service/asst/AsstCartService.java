package id.lariss.store.service.asst;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;

public interface AsstCartService {
    CartItemDTO addToCart(CartItemDTO cartItemDTO);

    CartDTO getCart(Long customerId);
}
