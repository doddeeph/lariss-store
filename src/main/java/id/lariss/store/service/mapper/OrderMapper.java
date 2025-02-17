package id.lariss.store.service.mapper;

import id.lariss.store.domain.Customer;
import id.lariss.store.domain.Order;
import id.lariss.store.domain.OrderItem;
import id.lariss.store.service.dto.CustomerDTO;
import id.lariss.store.service.dto.OrderDTO;
import id.lariss.store.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerFirstName")
    OrderDTO toDto(Order s);

    @Named("customerFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    CustomerDTO toDtoCustomerFirstName(Customer customer);

    @Mapping(target = "order", ignore = true)
    OrderItemDTO toOrderItemDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);
}
