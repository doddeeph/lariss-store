package id.lariss.store.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
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

    @JsonView(Views.Public.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String productName;

    @JsonView(Views.Internal.class)
    private String description;

    @JsonView(Views.Internal.class)
    private CurrencyCode currencyCode;

    @JsonView(Views.Internal.class)
    private CategoryDTO category;
}
