package id.lariss.store.service.mapper;

import id.lariss.store.domain.Cart;
import id.lariss.store.domain.CartItem;
import id.lariss.store.domain.ProductVariant;
import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;
import id.lariss.store.service.dto.ProductVariantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CartItem} and its DTO {@link CartItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartItemMapper extends FormattedPriceMapper<CartItemDTO, CartItem> {
    @Mapping(
        target = "formattedPrice",
        expression = "java(formattedPrice(s.getPrice(), s.getProductVariant().getProduct().getCurrencyCode()))"
    )
    @Mapping(target = "productVariant", source = "productVariant", qualifiedByName = "productVariantId")
    @Mapping(target = "cart", source = "cart", qualifiedByName = "cartId")
    CartItemDTO toDto(CartItem s);

    @Named("productVariantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductVariantDTO toDtoProductVariantId(ProductVariant productVariant);

    @Named("cartId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CartDTO toDtoCartId(Cart cart);
}
