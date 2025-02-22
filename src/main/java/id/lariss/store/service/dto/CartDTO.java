package id.lariss.store.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link id.lariss.store.domain.Cart} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartDTO implements Serializable {

    @JsonView(Views.Public.class)
    private Long id;

    @JsonView(Views.Public.class)
    private Instant createdDate;

    @JsonView(Views.Public.class)
    private CustomerDTO customer;

    @JsonView(Views.Public.class)
    Set<CartItemDTO> cartItems;
}
