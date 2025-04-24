package id.lariss.store.service.v1;

import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.OrderDTO;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderDTO placeOrder(CartDTO cartDTO, String shippingAddress);

    Map<String, Object> placeOrder(Long customerId, String shippingAddress);

    List<Map<String, Object>> getOrders(Long customerId);
}
