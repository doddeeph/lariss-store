package id.lariss.store.service.v1;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.OrderDTO;
import java.util.Set;

public interface AsstOrderService {
    OrderDTO placeOrder(CartDTO cartDTO);

    Set<OrderDTO> getOrders(Long customerId);
}
