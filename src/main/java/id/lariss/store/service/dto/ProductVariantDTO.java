package id.lariss.store.service.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link id.lariss.store.domain.ProductVariant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDTO implements Serializable {

    @JsonView({ Views.Public.class })
    private Long id;

    @JsonView({ Views.Public.class })
    private String imageUrl;

    @DecimalMin(value = "0")
    @JsonView({ Views.Public.class })
    private BigDecimal price;

    @JsonView({ Views.Public.class })
    private String formattedPrice;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String color;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String processor;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String memory;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String storage;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String screen;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String connectivity;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String material;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String caseSize;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String strapColor;

    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String strapSize;

    @JsonView({ Views.Internal.class })
    private ProductDTO product;
}
