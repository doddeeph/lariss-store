package id.lariss.store.service.mapper;

import id.lariss.store.domain.*;
import id.lariss.store.domain.enumeration.CurrencyCode;
import id.lariss.store.service.dto.CustomerDTO;
import id.lariss.store.service.dto.OrderDTO;
import id.lariss.store.service.dto.OrderItemDTO;
import id.lariss.store.service.dto.ProductVariantDTO;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends FormattedPriceMapper<OrderDTO, Order> {
    @Mapping(target = "totalPrice", expression = "java(totalPrice(s.getOrderItems()))")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerFirstName")
    OrderDTO toDto(Order s);

    @Named("customerFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    CustomerDTO toDtoCustomerFirstName(Customer customer);

    @Mapping(
        target = "formattedPrice",
        expression = "java(formattedPrice(orderItem.getPrice(), orderItem.getProductVariant().getProduct().getCurrencyCode()))"
    )
    @Mapping(target = "order", ignore = true)
    OrderItemDTO toOrderItemDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);

    @Mapping(
        target = "formattedPrice",
        expression = "java(formattedPrice(productVariant.getPrice(), productVariant.getProduct().getCurrencyCode()))"
    )
    ProductVariantDTO toProductVariantDto(ProductVariant productVariant);

    default String totalPrice(Set<OrderItem> orderItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        CurrencyCode currencyCode = CurrencyCode.IDR;
        if (CollectionUtils.isNotEmpty(orderItems)) {
            totalPrice = orderItems
                .stream()
                .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            currencyCode = orderItems
                .stream()
                .map(OrderItem::getProductVariant)
                .map(ProductVariant::getProduct)
                .map(Product::getCurrencyCode)
                .findFirst()
                .orElse(CurrencyCode.IDR);
        }
        return formattedPrice(totalPrice, currencyCode);
    }
}
