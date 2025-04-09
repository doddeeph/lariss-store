package id.lariss.store.service.impl.v1;

import id.lariss.store.service.CartItemService;
import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;
import id.lariss.store.service.dto.CustomerDTO;
import id.lariss.store.service.v1.CartService;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("CartServiceImplV1")
public class CartServiceImpl implements CartService {

    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartItemService cartItemService;
    private final id.lariss.store.service.CartService cartService;

    public CartServiceImpl(CartItemService cartItemService, id.lariss.store.service.CartService cartService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @Override
    public CartDTO addToCart(CartItemDTO cartItemDTO) {
        if (Objects.nonNull(cartItemDTO.getCart())) {
            CartDTO cartDTO = getExistOrCreateNewCart(cartItemDTO.getCart().getId(), cartItemDTO.getCart().getCustomer());

            // new cart
            if (Objects.isNull(cartDTO.getId())) {
                cartDTO = cartService.save(cartDTO);
                cartItemDTO.setCart(cartDTO);
                CartItemDTO savedCartItemDTO = cartItemService.save(cartItemDTO);
                cartDTO.getCartItems().add(savedCartItemDTO);
                return cartDTO;
            }

            // exist cart
            Long cartId = cartDTO.getId();
            Optional<CartItemDTO> opUpdatedCartItemDTO = cartDTO
                .getCartItems()
                .stream()
                .filter(existCartItemDTO -> cartItemDTO.getProductVariant().getId().equals(existCartItemDTO.getProductVariant().getId()))
                .peek(existCartItemDTO -> {
                    existCartItemDTO.setCart(CartDTO.builder().id(cartId).build());
                    existCartItemDTO.setPrice(cartItemDTO.getPrice());
                    existCartItemDTO.setQuantity(existCartItemDTO.getQuantity() + cartItemDTO.getQuantity());
                })
                .findFirst();

            // exist cart item
            if (opUpdatedCartItemDTO.isPresent()) {
                cartItemService.update(opUpdatedCartItemDTO.orElseThrow());
                return cartDTO;
            }

            // new cart item
            cartItemDTO.setCart(cartDTO);
            CartItemDTO savedCartItemDTO = cartItemService.save(cartItemDTO);

            cartDTO.getCartItems().add(savedCartItemDTO);
            return cartService.save(cartDTO);
        }

        throw new RuntimeException("Cart not found.");
    }

    @Override
    public CartDTO getCart(Long customerId) {
        return cartService.findOneByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Cart not found."));
    }

    private CartDTO getExistOrCreateNewCart(Long cartId, CustomerDTO customerDTO) {
        if (Objects.nonNull(cartId)) {
            return cartService.findOne(cartId).orElseThrow(() -> new RuntimeException("Cart not found."));
        } else if (Objects.nonNull(customerDTO) && Objects.nonNull(customerDTO.getId())) { // by Customer ID
            return cartService
                .findOneByCustomerId(customerDTO.getId())
                .orElse(CartDTO.builder().customer(customerDTO).createdDate(Instant.now()).cartItems(new HashSet<>()).build());
        }
        throw new RuntimeException("Cart not found.");
    }
}
