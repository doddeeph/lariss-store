package id.lariss.store.service.dto;

import id.lariss.store.domain.enumeration.CurrencyCode;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link id.lariss.store.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO implements Serializable {

    private Long id;

    private String productName;

    private String description;

    private CurrencyCode currencyCode;

    private CategoryDTO category;
}
