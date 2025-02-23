package id.lariss.store.service.mapper;

import id.lariss.store.domain.*;
import id.lariss.store.domain.enumeration.CurrencyCode;
import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;
import id.lariss.store.service.dto.CustomerDTO;
import java.math.BigDecimal;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartMapper extends FormattedPriceMapper<CartDTO, Cart> {
    @Mapping(target = "totalPrice", expression = "java(totalPrice(s.getCartItems()))")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerFirstName")
    CartDTO toDto(Cart s);

    @Named("customerFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    CustomerDTO toDtoCustomerFirstName(Customer customer);

    @Mapping(
        target = "formattedPrice",
        expression = "java(formattedPrice(cartItem.getPrice(), cartItem.getProductVariant().getProduct().getCurrencyCode()))"
    )
    @Mapping(target = "cart", ignore = true)
    CartItemDTO toCartItemDto(CartItem cartItem);

    default String totalPrice(Set<CartItem> cartItems) {
        BigDecimal totalPrice = cartItems
            .stream()
            .map(cartItem -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        CurrencyCode currencyCode = cartItems
            .stream()
            .map(CartItem::getProductVariant)
            .map(ProductVariant::getProduct)
            .map(Product::getCurrencyCode)
            .findFirst()
            .get();
        return formattedPrice(totalPrice, currencyCode);
    }
}
