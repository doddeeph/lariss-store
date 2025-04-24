package id.lariss.store.service.impl.v1;

import id.lariss.store.domain.enumeration.OrderStatus;
import id.lariss.store.service.CartItemService;
import id.lariss.store.service.CartService;
import id.lariss.store.service.OrderItemService;
import id.lariss.store.service.dto.CartDTO;
import id.lariss.store.service.dto.OrderDTO;
import id.lariss.store.service.dto.OrderItemDTO;
import id.lariss.store.service.v1.OrderService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("OrderServiceImplV1")
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final id.lariss.store.service.OrderService orderService;

    public OrderServiceImpl(
        CartItemService cartItemService,
        CartService cartService,
        OrderItemService orderItemService,
        id.lariss.store.service.OrderService orderService
    ) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    @Override
    public OrderDTO placeOrder(CartDTO cartDTO, String shippingAddress) {
        if (Objects.nonNull(cartDTO.getId())) {
            cartDTO = cartService.findOne(cartDTO.getId()).orElseThrow(() -> new RuntimeException("Cart not found."));
        } else if (Objects.nonNull(cartDTO.getCustomer().getId())) {
            cartDTO = cartService
                .findOneByCustomerId(cartDTO.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Cart not found."));
        }

        OrderDTO orderDTO = orderService.save(
            OrderDTO.builder()
                .orderDate(Instant.now())
                .customer(cartDTO.getCustomer())
                .status(OrderStatus.PENDING)
                .shippingAddress(shippingAddress)
                .build()
        );

        Set<OrderItemDTO> orderItemDTOs = cartDTO
            .getCartItems()
            .stream()
            .map(cartItemDTO ->
                OrderItemDTO.builder()
                    .quantity(cartItemDTO.getQuantity())
                    .price(cartItemDTO.getPrice())
                    .productVariant(cartItemDTO.getProductVariant())
                    .order(orderDTO)
                    .build()
            )
            .collect(Collectors.toSet());

        Set<OrderItemDTO> savedOrderItemDTOs = orderItemService.saveAll(orderItemDTOs);
        orderDTO.setOrderItems(savedOrderItemDTOs);

        cartItemService.deleteAll(cartDTO.getCartItems());
        cartService.delete(cartDTO.getId());

        return orderDTO;
    }

    @Override
    public Map<String, Boolean> placeOrder(Long customerId, String shippingAddress) {
        return cartService
            .findOneByCustomerId(customerId)
            .map(cartDTO -> placeOrder(cartDTO, shippingAddress))
            .map(orderDTO -> Map.of("success", true))
            .orElse(Map.of("success", false));
    }

    @Override
    public List<Map<String, Object>> getOrders(Long customerId) {
        return orderService
            .findAllByCustomerId(customerId)
            .stream()
            .map(orderDTO -> {
                Map<String, Object> order = new HashMap<>();
                order.put("orderId", generateOrderId(orderDTO.getOrderDate(), orderDTO.getId()));
                order.put("status", orderDTO.getStatus());
                order.put("orderDate", getOrderDateAsString(orderDTO.getOrderDate(), "dd MMMM yyyy HH:mm:ss"));
                order.put("totalPrice", orderDTO.getTotalPrice());
                List<Map<String, Object>> orderItems = orderDTO
                    .getOrderItems()
                    .stream()
                    .map(orderItemDTO -> {
                        Map<String, Object> orderItem = new HashMap<>();
                        orderItem.put("summary", orderItemDTO.getProductVariant().getSummary());
                        orderItem.put("quantity", orderItemDTO.getQuantity());
                        return orderItem;
                    })
                    .toList();
                order.put("orderItems", orderItems);
                return order;
            })
            .toList();
    }

    private String getOrderDateAsString(Instant orderDate, String pattern) {
        ZonedDateTime zonedDateTime = orderDate.atZone(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, new Locale("id", "ID"));
        return zonedDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    private String generateOrderId(Instant orderDate, Long id) {
        String datePart = getOrderDateAsString(orderDate, "yyyyMMdd");
        return String.format("INV/%s/%d", datePart, id);
    }
}
