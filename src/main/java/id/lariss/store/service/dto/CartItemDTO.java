package id.lariss.store.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link id.lariss.store.domain.CartItem} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartItemDTO implements Serializable {

    @JsonView({ Views.Public.class })
    private Long id;

    @Min(value = 1)
    @JsonView({ Views.Public.class })
    private Integer quantity;

    @DecimalMin(value = "0")
    @JsonView({ Views.Public.class })
    private BigDecimal price;

    @JsonView({ Views.Public.class })
    private ProductVariantDTO productVariant;

    @JsonView({ Views.Internal.class })
    private CartDTO cart;
}
