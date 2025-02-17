package id.lariss.store.service.mapper;

import id.lariss.store.domain.Cart;
import id.lariss.store.domain.CartItem;
import id.lariss.store.domain.Customer;
import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.CartItemDTO;
import id.lariss.store.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerFirstName")
    CartDTO toDto(Cart s);

    @Named("customerFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    CustomerDTO toDtoCustomerFirstName(Customer customer);

    @Mapping(target = "cart", ignore = true)
    CartItemDTO toCartItemDto(CartItem cartItem);
}
