package id.lariss.store.service.dto;

import id.lariss.store.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link id.lariss.store.domain.Order} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDTO implements Serializable {

    private Long id;

    private OrderStatus status;

    private Instant orderDate;

    private CustomerDTO customer;

    private Set<OrderItemDTO> orderItems;
}
