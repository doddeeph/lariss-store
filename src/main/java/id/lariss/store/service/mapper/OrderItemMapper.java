package id.lariss.store.service.mapper;

import id.lariss.store.domain.Order;
import id.lariss.store.domain.OrderItem;
import id.lariss.store.domain.ProductVariant;
import id.lariss.store.service.dto.OrderDTO;
import id.lariss.store.service.dto.OrderItemDTO;
import id.lariss.store.service.dto.ProductVariantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends FormattedPriceMapper<OrderItemDTO, OrderItem> {
    @Mapping(
        target = "formattedPrice",
        expression = "java(formattedPrice(s.getPrice(), s.getProductVariant().getProduct().getCurrencyCode()))"
    )
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    @Mapping(target = "productVariant", source = "productVariant", qualifiedByName = "productVariantId")
    OrderItemDTO toDto(OrderItem s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("productVariantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductVariantDTO toDtoProductVariantId(ProductVariant productVariant);
}
