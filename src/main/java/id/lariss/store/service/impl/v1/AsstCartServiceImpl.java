package id.lariss.store.service.impl.v1;

import id.lariss.store.service.CartItemService;
import id.lariss.store.service.CartService;
import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;
import id.lariss.store.service.v1.AsstCartService;
import java.time.Instant;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AsstCartServiceImpl implements AsstCartService {

    private static final Logger LOG = LoggerFactory.getLogger(AsstCartServiceImpl.class);

    private final CartItemService cartItemService;
    private final CartService cartService;

    public AsstCartServiceImpl(CartItemService cartItemService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @Override
    public CartItemDTO addToCart(CartItemDTO cartItemDTO) {
        if (Objects.isNull(cartItemDTO.getCart().getId())) {
            CartDTO cartDTO = cartService.save(
                CartDTO.builder().customer(cartItemDTO.getCart().getCustomer()).createdDate(Instant.now()).build()
            );
            cartItemDTO.setCart(cartDTO);
        }
        return cartItemService.save(cartItemDTO);
    }

    @Override
    public CartDTO getCart(Long customerId) {
        return cartService.findOneByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Cart not found."));
    }
}
