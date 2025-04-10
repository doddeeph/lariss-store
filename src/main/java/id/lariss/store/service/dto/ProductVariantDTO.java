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

    @JsonIgnore
    @JsonView({ Views.Public.class })
    private String formattedPrice;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String color;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String processor;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String memory;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String storage;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String screen;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String connectivity;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String material;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String caseSize;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String strapColor;

    @JsonIgnore
    @JsonView({ Views.Public.class })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String strapSize;

    @JsonView({ Views.Public.class })
    private String summary;

    @JsonView({ Views.Internal.class })
    private ProductDTO product;
}
