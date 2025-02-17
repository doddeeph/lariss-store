package id.lariss.store.service.dto;

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

    private Long id;

    private String imageUrl;

    @DecimalMin(value = "0")
    private BigDecimal price;

    private String color;

    private String processor;

    private String memory;

    private String storage;

    private String screen;

    private String connectivity;

    private String material;

    private String caseSize;

    private String strapColor;

    private String strapSize;

    private ProductDTO product;
}
