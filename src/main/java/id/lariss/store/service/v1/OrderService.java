package id.lariss.store.service.v1;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.OrderDTO;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderDTO placeOrder(CartDTO cartDTO);

    Map<String, Boolean> placeOrder(Long customerId);

    List<Map<String, Object>> getOrders(Long customerId);
}
